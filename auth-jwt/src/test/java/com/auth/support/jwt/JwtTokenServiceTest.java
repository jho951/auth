package com.auth.support.jwt;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.auth.api.model.Principal;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

class JwtTokenServiceTest {

	private static final String SECRET = "01234567890123456789012345678901";
	private final JwtTokenService tokenService = new JwtTokenService(SECRET, 60, 120);

	@Test
	void issueAccessToken_RoundsTripRolesAndAttributes() {
		Principal principal = new Principal("user-1", List.of("ADMIN"), Map.of("dept", "IT"));
		String token = tokenService.issueAccessToken(principal);

		Principal verified = tokenService.verifyAccessToken(token);

		assertThat(verified.getUserId()).isEqualTo("user-1");
		assertThat(verified.getAuthorities()).containsExactly("ADMIN");
		assertThat(verified.getAttributes()).containsEntry("dept", "IT");
	}

	@Test
	void accessTokenWithoutRolesReturnsEmptyList() {
		Principal principal = new Principal("user-2");
		String token = tokenService.issueAccessToken(principal);

		assertThat(tokenService.verifyAccessToken(token).getAuthorities()).isEmpty();
	}

	@Test
	void issueAccessToken_WritesAuthoritiesAndRolesClaims() {
		Principal principal = new Principal("user-1", List.of("ADMIN"), Map.of());
		String token = tokenService.issueAccessToken(principal);

		Claims claims = parseClaims(token);
		assertThat(claims.get("authorities")).isEqualTo(List.of("ADMIN"));
		assertThat(claims.get("roles")).isEqualTo(List.of("ADMIN"));
	}

	@Test
	void verifyAccessToken_FallsBackToRolesClaimWhenAuthoritiesMissing() {
		String token = buildToken(Map.of("roles", List.of("ADMIN")), "access");
		Principal verified = tokenService.verifyAccessToken(token);
		assertThat(verified.getAuthorities()).containsExactly("ADMIN");
	}

	@Test
	void verifyAccessToken_PrefersAuthoritiesOverRolesClaim() {
		String token = buildToken(Map.of("authorities", List.of("A"), "roles", List.of("B")), "access");
		Principal verified = tokenService.verifyAccessToken(token);
		assertThat(verified.getAuthorities()).containsExactly("A");
	}

	@Test
	void verifyRefreshToken_PrefersAuthoritiesOverRolesClaim() {
		String token = buildToken(Map.of("authorities", List.of("REFRESH_A"), "roles", List.of("REFRESH_B")), "refresh");
		Principal verified = tokenService.verifyRefreshToken(token);
		assertThat(verified.getAuthorities()).containsExactly("REFRESH_A");
	}

	private Claims parseClaims(String token) {
		Key key = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
		return Jwts.parserBuilder()
			.setSigningKey(key)
			.build()
			.parseClaimsJws(token)
			.getBody();
	}

	private String buildToken(Map<String, Object> claims, String type) {
		Key key = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
		Date now = new Date();
		Date exp = new Date(now.getTime() + 60_000L);
		return Jwts.builder()
			.setSubject("user-1")
			.addClaims(claims)
			.claim("token_type", type)
			.setIssuedAt(now)
			.setExpiration(exp)
			.signWith(key, SignatureAlgorithm.HS256)
			.compact();
	}
}
