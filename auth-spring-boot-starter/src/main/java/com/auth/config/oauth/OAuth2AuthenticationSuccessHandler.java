package com.auth.config.oauth;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import com.auth.api.model.OAuth2UserIdentity;
import com.auth.api.model.Principal;
import com.auth.api.model.Tokens;
import com.auth.common.utils.Strings;
import com.auth.config.controller.RefreshCookieWriter;
import com.auth.config.jwt.AuthJwtProperties;
import com.auth.spi.OAuth2PrincipalResolver;
import com.auth.spi.RefreshTokenStore;
import com.auth.spi.TokenService;

import java.time.Instant;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

/**
 * OAuth2 로그인 성공 시 내부 Principal 매핑과 JWT 발급을 수행합니다.
 */
public final class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	private final OAuth2PrincipalResolver principalResolver;
	private final TokenService tokenService;
	private final RefreshTokenStore refreshTokenStore;
	private final AuthJwtProperties jwtProperties;
	private final RefreshCookieWriter refreshCookieWriter;

	public OAuth2AuthenticationSuccessHandler(
		OAuth2PrincipalResolver principalResolver,
		TokenService tokenService,
		RefreshTokenStore refreshTokenStore,
		AuthJwtProperties jwtProperties,
		RefreshCookieWriter refreshCookieWriter
	) {
		this.principalResolver = principalResolver;
		this.tokenService = tokenService;
		this.refreshTokenStore = refreshTokenStore;
		this.jwtProperties = jwtProperties;
		this.refreshCookieWriter = refreshCookieWriter;
	}

	@Override
	public void onAuthenticationSuccess(
		HttpServletRequest request,
		HttpServletResponse response,
		Authentication authentication
	) throws IOException, ServletException {
		if (!(authentication instanceof OAuth2AuthenticationToken oauthToken)) {
			throw new ServletException("Unsupported authentication type: " + authentication.getClass().getName());
		}
		OAuth2AuthenticatedPrincipal oauthPrincipal = oauthToken.getPrincipal();

		OAuth2UserIdentity identity = toIdentity(oauthToken, oauthPrincipal);
		Principal principal = principalResolver.resolve(identity);
		Tokens tokens = issueTokens(principal);

		ResponseEntity<String> entity = refreshCookieWriter.write(tokens, ResponseEntity.ok(toJson(tokens)));
		writeResponse(response, entity);
	}

	private Tokens issueTokens(Principal principal) {
		String accessToken = tokenService.issueAccessToken(principal);
		String refreshToken = tokenService.issueRefreshToken(principal);
		refreshTokenStore.save(principal.getUserId(), refreshToken, Instant.now().plusSeconds(jwtProperties.getRefreshSeconds()));
		return new Tokens(accessToken, refreshToken);
	}

	private OAuth2UserIdentity toIdentity(
		OAuth2AuthenticationToken authentication,
		OAuth2AuthenticatedPrincipal principal
	) {
		Map<String, Object> attributes = principal.getAttributes();
		return new OAuth2UserIdentity(
			authentication.getAuthorizedClientRegistrationId(),
			principal.getName(),
			readString(attributes, "email"),
			readString(attributes, "name"),
			attributes
		);
	}

	private String readString(Map<String, Object> attributes, String key) {
		return Strings.toStringOrNull(attributes.get(key));
	}

	private String toJson(Tokens tokens) {
		return "{\"accessToken\":\"" + escapeJson(tokens.getAccessToken()) + "\"}";
	}

	private String escapeJson(String value) {
		StringBuilder escaped = new StringBuilder(value.length() + 8);
		for (int i = 0; i < value.length(); i++) {
			char c = value.charAt(i);
			switch (c) {
				case '\\' -> escaped.append("\\\\");
				case '"' -> escaped.append("\\\"");
				case '\n' -> escaped.append("\\n");
				case '\r' -> escaped.append("\\r");
				case '\t' -> escaped.append("\\t");
				default -> escaped.append(c);
			}
		}
		return escaped.toString();
	}

	private void writeResponse(HttpServletResponse response, ResponseEntity<String> entity) throws IOException {
		response.setStatus(entity.getStatusCode().value());
		response.setCharacterEncoding(StandardCharsets.UTF_8.name());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);

		for (Map.Entry<String, java.util.List<String>> header : entity.getHeaders().entrySet()) {
			if (HttpHeaders.CONTENT_TYPE.equalsIgnoreCase(header.getKey())) {
				continue;
			}
			for (String value : header.getValue()) {
				response.addHeader(header.getKey(), value);
			}
		}

		String body = entity.getBody();
		if (body != null) {
			response.getWriter().write(body);
		}
	}
}
