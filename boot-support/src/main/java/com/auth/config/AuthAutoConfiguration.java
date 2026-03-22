package com.auth.config;

import com.auth.config.controller.RefreshCookieWriter;
import com.auth.config.controller.RefreshTokenExtractor;
import com.auth.config.security.AuthOncePerRequestFilter;
import com.auth.core.service.AuthService;
import com.auth.config.jwt.AuthJwtProperties;
import com.auth.support.jwt.JwtTokenService;
import com.auth.support.password.bcrypt.BCryptPasswordVerifier;
import com.auth.support.refresh.memory.InMemoryRefreshTokenStore;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.*;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.time.Clock;
import java.time.Duration;

import com.auth.spi.PasswordVerifier;
import com.auth.spi.RefreshTokenStore;
import com.auth.spi.TokenService;
import com.auth.spi.UserFinder;

@AutoConfiguration
@EnableConfigurationProperties({AuthProperties.class, AuthJwtProperties.class})
public class AuthAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean(RefreshTokenStore.class)
	public RefreshTokenStore inMemoryRefreshTokenStore() {
		return new InMemoryRefreshTokenStore();
	}


	@Bean
	@ConditionalOnMissingBean(TokenService.class)
	@ConditionalOnProperty(prefix = "auth.jwt", name = "secret")
	public TokenService defaultTokenService(AuthJwtProperties props) {
		return new JwtTokenService(props.getSecret(), props.getAccessSeconds(), props.getRefreshSeconds());
	}

	@Bean
	@ConditionalOnMissingBean(PasswordVerifier.class)
	public PasswordVerifier bcryptPasswordVerifier() {
		return new BCryptPasswordVerifier();
	}

	@Bean
	public AuthService authService(
		UserFinder userFinder,
		PasswordVerifier passwordVerifier,
		TokenService tokenService,
		RefreshTokenStore refreshTokenStore,
		AuthJwtProperties jwtProps
	) {
		return new AuthService(
			userFinder,
			passwordVerifier,
			tokenService,
			refreshTokenStore,
			Duration.ofSeconds(jwtProps.getRefreshSeconds())
		);
	}

	@Bean
	@ConditionalOnClass(name = "org.springframework.web.filter.OncePerRequestFilter")
	@ConditionalOnMissingBean(AuthOncePerRequestFilter.class)
	public AuthOncePerRequestFilter authOncePerRequestFilter(TokenService tokenService, AuthProperties props) {
		return new AuthOncePerRequestFilter(tokenService, props);
	}

	@Bean
	@ConditionalOnMissingBean(RefreshCookieWriter.class)
	public RefreshCookieWriter refreshCookieWriter(AuthProperties props, AuthJwtProperties jwtProps) {
		return new RefreshCookieWriter(props, jwtProps);
	}

	@Bean
	@ConditionalOnMissingBean(RefreshTokenExtractor.class)
	public RefreshTokenExtractor refreshTokenExtractor(AuthProperties props) {
		return new RefreshTokenExtractor(props);
	}
}
