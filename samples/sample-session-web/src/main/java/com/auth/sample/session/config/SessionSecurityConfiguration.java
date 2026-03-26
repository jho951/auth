package com.auth.sample.session.config;

import com.auth.session.security.SessionAuthenticationFilter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SessionSecurityConfiguration {

	@Bean
	SecurityFilterChain sessionSecurityChain(HttpSecurity http, SessionAuthenticationFilter sessionFilter) throws Exception {
		http
			.csrf(csrf -> csrf.disable())
			.sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.authorizeHttpRequests(auth -> auth
				.requestMatchers("/session/login", "/session/logout").permitAll()
				.anyRequest().authenticated()
			)
			.addFilterBefore(sessionFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}
}
