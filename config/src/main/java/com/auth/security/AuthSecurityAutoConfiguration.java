package com.auth.security;

import com.auth.AuthProperties;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.*;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.nio.charset.StandardCharsets;

@AutoConfiguration
@AutoConfigureAfter(name = "org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration")
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ConditionalOnClass(SecurityFilterChain.class)
@ConditionalOnProperty(prefix="auth", name="auto-security", havingValue="true", matchIfMissing=true)
public class AuthSecurityAutoConfiguration {

	@Bean
	@Order(Ordered.LOWEST_PRECEDENCE)
	@ConditionalOnMissingBean(SecurityFilterChain.class)
	public SecurityFilterChain authDefaultSecurityFilterChain(
		HttpSecurity http,
		AuthOncePerRequestFilter authFilter,
		AuthProperties props
	) throws Exception {

		http
			.csrf(csrf -> csrf.disable())
			.sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.httpBasic(basic -> basic.disable())
			.formLogin(form -> form.disable())
			.logout(logout -> logout.disable())
			.exceptionHandling(ex -> ex
				.authenticationEntryPoint((req, res, e) -> {
					res.setStatus(401);
					res.setCharacterEncoding(StandardCharsets.UTF_8.name());
					res.setContentType(MediaType.APPLICATION_JSON_VALUE);
					res.getWriter().write("{\"message\":\"UNAUTHORIZED\"}");
				})
				.accessDeniedHandler((req, res, e) -> {
					res.setStatus(403);
					res.setCharacterEncoding(StandardCharsets.UTF_8.name());
					res.setContentType(MediaType.APPLICATION_JSON_VALUE);
					res.getWriter().write("{\"message\":\"FORBIDDEN\"}");
				})
			)
			.authorizeHttpRequests(auth -> auth
				.requestMatchers("/auth/**").permitAll()
				.anyRequest().authenticated()
			);

		http.addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}
}
