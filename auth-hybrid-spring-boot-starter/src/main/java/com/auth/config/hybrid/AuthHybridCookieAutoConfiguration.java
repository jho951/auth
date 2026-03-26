package com.auth.config.hybrid;

import com.auth.config.AuthProperties;
import com.auth.config.controller.RefreshCookieWriter;
import com.auth.config.jwt.AuthJwtProperties;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@ConditionalOnBean(AuthJwtProperties.class)
public class AuthHybridCookieAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean(RefreshCookieWriter.class)
	public RefreshCookieWriter refreshCookieWriter(AuthProperties props, AuthJwtProperties jwtProps) {
		return new RefreshCookieWriter(props, jwtProps.getRefreshSeconds());
	}
}
