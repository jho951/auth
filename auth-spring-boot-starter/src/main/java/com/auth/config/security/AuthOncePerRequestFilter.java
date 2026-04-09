package com.auth.config.security;

import com.auth.api.model.Principal;
import com.auth.config.AuthProperties;
import com.auth.spi.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

/** Bearer 토큰을 검증하고 Principal이 전달한 authority/attribute 메타데이터를 SecurityContext에 저장합니다. */
public final class AuthOncePerRequestFilter extends OncePerRequestFilter {

	private static final Logger log = Logger.getLogger(AuthOncePerRequestFilter.class.getName());

	private final TokenService tokenService;
	private final AuthProperties props;

	/**
	 * 생성자
	 * @param tokenService
	 * @param props
	 */
	public AuthOncePerRequestFilter(TokenService tokenService, AuthProperties props) {
		this.tokenService = tokenService;
		this.props = props;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
		throws ServletException, IOException {
		try {
			String auth = request.getHeader("Authorization");

			if (auth != null && auth.startsWith(props.getBearerPrefix())) {
				String token = auth.substring(props.getBearerPrefix().length()).trim();

				// OSS auth: 검증된 토큰에서 Principal과 메타데이터를 복원합니다.
				Principal principal = tokenService.verifyAccessToken(token);

			// Spring Security 브리지: Principal이 전달한 authority/attribute 데이터를 GrantedAuthority로 변환합니다.
			Collection<? extends GrantedAuthority> authorities = extractAuthorities(principal);

				// Principal 자체를 SecurityContext에 보관합니다.
				var authentication = new UsernamePasswordAuthenticationToken(principal, null, authorities);

				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		} catch (Exception e) {
			log.log(Level.WARNING, "Failed to authenticate bearer token", e);
			SecurityContextHolder.clearContext();
		} finally {
			chain.doFilter(request, response);
		}
	}

	private Collection<? extends GrantedAuthority> extractAuthorities(Principal principal) {
		Set<String> authorityNames = new LinkedHashSet<>(principal.getAuthorities());
		addAttributeAuthorities(authorityNames, principal.getAttribute("authorities"));
		addAttributeAuthorities(authorityNames, principal.getAttribute("roles"));
		addAttributeAuthorities(authorityNames, principal.getAttribute("scopes"));
		addAttributeAuthorities(authorityNames, principal.getAttribute("authority"));
		return authorityNames.stream()
			.map(SimpleGrantedAuthority::new)
			.toList();
	}

	private void addAttributeAuthorities(Set<String> names, Object attributeValue) {
		if (attributeValue == null) return;
		toAuthorityStrings(attributeValue).forEach(names::add);
	}

	private List<String> toAuthorityStrings(Object raw) {
		if (raw instanceof Collection<?> values) {
			return values.stream()
				.filter(Objects::nonNull)
				.map(Object::toString)
				.map(String::trim)
				.filter(s -> !s.isEmpty())
				.toList();
		}
		String normalized = raw.toString().trim();
		return normalized.isEmpty() ? List.of() : List.of(normalized);
	}
}
