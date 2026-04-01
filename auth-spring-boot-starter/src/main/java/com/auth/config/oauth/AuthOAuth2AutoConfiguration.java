package com.auth.config.oauth;

import com.auth.config.controller.RefreshCookieWriter;
import com.auth.config.jwt.AuthJwtProperties;
import com.auth.spi.OAuth2PrincipalResolver;
import com.auth.spi.RefreshTokenStore;
import com.auth.spi.TokenService;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

/**
 * OAuth2 로그인 결과를 OSS auth의 내부 Principal 및 JWT 발급 흐름과 연결하는 자동 설정입니다.
 */
@AutoConfiguration
@AutoConfigureAfter(com.auth.config.hybrid.AuthHybridCookieAutoConfiguration.class)
@ConditionalOnClass(name = "org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter")
@ConditionalOnBean(OAuth2PrincipalResolver.class)
@ConditionalOnProperty(prefix = "auth.oauth2", name = "enabled", havingValue = "true", matchIfMissing = true)
public class AuthOAuth2AutoConfiguration {

	@Bean("authOAuth2AuthenticationSuccessHandler")
	@ConditionalOnBean({TokenService.class, RefreshTokenStore.class, AuthJwtProperties.class})
	@ConditionalOnMissingBean(name = "authOAuth2AuthenticationSuccessHandler")
	public AuthenticationSuccessHandler authOAuth2AuthenticationSuccessHandler(
		OAuth2PrincipalResolver principalResolver,
		TokenService tokenService,
		RefreshTokenStore refreshTokenStore,
		AuthJwtProperties jwtProperties,
		RefreshCookieWriter refreshCookieWriter
	) {
		return new OAuth2AuthenticationSuccessHandler(principalResolver, tokenService, refreshTokenStore, jwtProperties, refreshCookieWriter);
	}

	@Bean("authOAuth2AuthenticationFailureHandler")
	@ConditionalOnMissingBean(name = "authOAuth2AuthenticationFailureHandler")
	public AuthenticationFailureHandler authOAuth2AuthenticationFailureHandler() {
		return new OAuth2AuthenticationFailureHandler();
	}

}
