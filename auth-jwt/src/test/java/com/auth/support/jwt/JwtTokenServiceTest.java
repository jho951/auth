package com.auth.support.jwt;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.auth.api.model.Principal;

class JwtTokenServiceTest {

	private final JwtTokenService tokenService = new JwtTokenService("01234567890123456789012345678901", 60, 120);

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
}
