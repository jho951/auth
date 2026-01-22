package com.auth;

import com.auth.controller.AuthController;
import com.auth.security.AuthOncePerRequestFilter;
import com.auth.service.AuthService;
import com.auth.jwt.AuthJwtProperties;
import com.auth.jwt.JwtTokenService;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.*;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import auth.PasswordVerifier;
import auth.RefreshTokenStore;
import auth.TokenService;
import auth.UserFinder;

@AutoConfiguration
@EnableConfigurationProperties({AuthProperties.class, AuthJwtProperties.class})
public class AuthAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean(RefreshTokenStore.class)
	public RefreshTokenStore inMemoryRefreshTokenStore() {
		return new RefreshTokenStore() {
			private final Map<String, Entry> store = new ConcurrentHashMap<>();
			private final Clock clock = Clock.systemUTC();

			@Override
			public void save(String userId, String refreshToken, Instant expiresAt) {
				store.put(key(userId, refreshToken), new Entry(expiresAt));
			}

			@Override
			public boolean exists(String userId, String refreshToken) {
				Entry e = store.get(key(userId, refreshToken));
				if (e == null) return false;
				if (Instant.now(clock).isAfter(e.expiresAt)) {
					store.remove(key(userId, refreshToken));
					return false;
				}
				return true;
			}

			@Override
			public void revoke(String userId, String refreshToken) {
				store.remove(key(userId, refreshToken));
			}

			private String key(String userId, String refreshToken) {
				return userId + "::" + refreshToken;
			}

			final class Entry {
				final Instant expiresAt;
				Entry(Instant expiresAt) { this.expiresAt = expiresAt; }
			}
		};
	}


	@Bean
	@ConditionalOnMissingBean(TokenService.class)
	@ConditionalOnProperty(prefix = "auth.jwt", name = "secret")
	public TokenService defaultTokenService(AuthJwtProperties props) {
		return new JwtTokenService(props.getSecret(), props.getAccessSeconds(), props.getRefreshSeconds());
	}

	@Bean
	@ConditionalOnClass(name = "org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder")
	@ConditionalOnMissingBean(PasswordVerifier.class)
	public PasswordVerifier bcryptPasswordVerifier() {
		return new PasswordVerifier() {
			private final org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder enc =
				new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder();

			@Override
			public boolean matches(String rawPassword, String storedHash) {
				return enc.matches(rawPassword, storedHash);
			}
		};
	}

	@Bean
	public AuthService authService(
		UserFinder userFinder,
		PasswordVerifier passwordVerifier,
		TokenService tokenService,
		RefreshTokenStore refreshTokenStore
	) {
		return new AuthService(
			userFinder,
			passwordVerifier,
			tokenService,
			refreshTokenStore,
			Duration.ofDays(14)
		);
	}

	@Bean
	@ConditionalOnClass(name = "org.springframework.web.filter.OncePerRequestFilter")
	@ConditionalOnMissingBean(AuthOncePerRequestFilter.class)
	public AuthOncePerRequestFilter authOncePerRequestFilter(TokenService tokenService, AuthProperties props) {
		return new AuthOncePerRequestFilter(tokenService, props);
	}

	@Bean
	@ConditionalOnProperty(prefix = "auth", name = "endpoints-enabled", havingValue = "true", matchIfMissing = true)
	@ConditionalOnMissingBean(AuthController.class)
	public AuthController authController(AuthService authService, AuthProperties props) {
		return new AuthController(authService, props);
	}
}
