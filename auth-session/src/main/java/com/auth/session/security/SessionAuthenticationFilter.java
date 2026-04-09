package com.auth.session.security;

import com.auth.api.model.Principal;
import com.auth.session.SessionAuthenticationProvider;
import com.auth.session.SessionCookieExtractor;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

public final class SessionAuthenticationFilter extends OncePerRequestFilter {

	private final SessionCookieExtractor cookieExtractor;
	private final SessionAuthenticationProvider provider;

	/**
	 * 생성자
	 * @param cookieExtractor
	 * @param provider
	 */
	public SessionAuthenticationFilter(SessionCookieExtractor cookieExtractor, SessionAuthenticationProvider provider) {
		this.cookieExtractor = cookieExtractor;
		this.provider = provider;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
		throws ServletException, IOException {
		try {
			cookieExtractor.extract(request)
				.flatMap(provider::authenticate)
				.ifPresent(this::authenticate);
		} catch (RuntimeException e) {
			SecurityContextHolder.clearContext();
		}
		filterChain.doFilter(request, response);
	}

	private void authenticate(Principal principal) {
		Collection<? extends GrantedAuthority> authorities = principal.getAuthorities().stream()
			.map(SimpleGrantedAuthority::new)
			.toList();

		var auth = new UsernamePasswordAuthenticationToken(principal, null, authorities);
		SecurityContextHolder.getContext().setAuthentication(auth);
	}
}
