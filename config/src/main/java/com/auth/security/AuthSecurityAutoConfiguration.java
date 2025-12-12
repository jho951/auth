package com.auth.security;


import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.*;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth.AuthProperties;

@AutoConfiguration
@ConditionalOnClass(SecurityFilterChain.class)
@ConditionalOnProperty(prefix = "auth", name = "auto-security", havingValue = "true", matchIfMissing = true)
public class AuthSecurityAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean(SecurityFilterChain.class)
	public SecurityFilterChain authDefaultSecurityFilterChain(
		HttpSecurity http,
		AuthOncePerRequestFilter authFilter,
		AuthProperties props
	) throws Exception {

		// 기본 정책(원하면 바꿔도 됨)
		http.csrf(csrf -> csrf.disable());

		http.authorizeHttpRequests(auth -> auth
			.requestMatchers("/auth/**").permitAll()
			.anyRequest().authenticated()
		);

		http.addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}
}
