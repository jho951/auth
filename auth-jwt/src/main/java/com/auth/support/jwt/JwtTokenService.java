package com.auth.support.jwt;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.auth.api.exception.AuthException;
import com.auth.api.exception.AuthFailureReason;
import com.auth.api.model.Principal;
import com.auth.common.utils.Strings;
import com.auth.spi.TokenService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

public class JwtTokenService implements TokenService {

	private final Key key;
	private final long accessSeconds;
	private final long refreshSeconds;

	private static final String KEY_TOKEN_TYPE = "token_type";
	private static final String KEY_AUTHORITIES = "authorities";
	private static final String KEY_ROLES = "roles";

	private String buildToken(Principal principal, long ttlSeconds, String type) {
		Date now = new Date();
		Date exp = new Date(now.getTime() + (ttlSeconds * 1000L));

		Map<String, Object> claims = new HashMap<>(principal.getAttributes());
		if (!principal.getAuthorities().isEmpty()) {
			claims.put(KEY_AUTHORITIES, principal.getAuthorities());
			claims.put(KEY_ROLES, principal.getAuthorities());
		}

		return Jwts.builder()
			.setSubject(principal.getUserId())
			.addClaims(claims)
			.claim(KEY_TOKEN_TYPE, type)
			.setIssuedAt(now)
			.setExpiration(exp)
			.signWith(key, SignatureAlgorithm.HS256)
			.compact();
	}

	/**
	 * 생성자
	 * @param secret
	 * @param accessSeconds
	 * @param refreshSeconds
	 */
	public JwtTokenService(String secret, long accessSeconds, long refreshSeconds) {
		if (Strings.isBlank(secret)) throw new AuthException(AuthFailureReason.INVALID_INPUT, "auth.jwt.secret must not be blank");
		byte[] bytes = secret.getBytes(StandardCharsets.UTF_8);
		if (bytes.length < 32) throw new AuthException(AuthFailureReason.INVALID_INPUT, "auth.jwt.secret must be at least 32 bytes for HS256");

		this.key = Keys.hmacShaKeyFor(bytes);
		this.accessSeconds = accessSeconds;
		this.refreshSeconds = refreshSeconds;
	}


	private Principal parseAndToPrincipal(String token, String expectedType) {
		try {
			Claims claims = Jwts.parserBuilder()
				.setSigningKey(key)
				.build()
				.parseClaimsJws(token)
				.getBody();

			String type = claims.get(KEY_TOKEN_TYPE, String.class);
			if (type == null) throw new AuthException(AuthFailureReason.INVALID_TOKEN, "invalid token type");
			if (!type.equals(expectedType)) throw new AuthException(AuthFailureReason.INVALID_TOKEN, "invalid token type");

			String userId = claims.getSubject();

			Map<String, Object> attributes = new HashMap<>(claims);
			attributes.remove(Claims.SUBJECT);
			attributes.remove(Claims.ISSUED_AT);
			attributes.remove(Claims.EXPIRATION);
			attributes.remove(KEY_TOKEN_TYPE);
			List<String> authorities = toAuthorities(attributes.remove(KEY_AUTHORITIES));
			if (authorities.isEmpty()) {
				authorities = toAuthorities(attributes.remove(KEY_ROLES));
			}
			return new Principal(userId, authorities, attributes);
		} catch (JwtException | IllegalArgumentException e) {
			throw new AuthException(AuthFailureReason.INVALID_TOKEN, "invalid/expired token", e);
		}
	}

	private List<String> toAuthorities(Object rawAuthorityData) {
		if (rawAuthorityData instanceof List<?> list) {
			return list.stream()
				.filter(Objects::nonNull)
				.map(Object::toString)
				.toList();
		}
		if (rawAuthorityData instanceof String value) return List.of(value);
		return List.of();
	}

	@Override
	public String issueAccessToken(Principal principal) {
		return buildToken(principal, accessSeconds, "access");
	}
	@Override
	public String issueRefreshToken(Principal principal) {
		return buildToken(principal, refreshSeconds, "refresh");
	}
	@Override
	public Principal verifyAccessToken(String token) {
		return parseAndToPrincipal(token, "access");
	}
	@Override
	public Principal verifyRefreshToken(String token) {
		return parseAndToPrincipal(token, "refresh");
	}


}
