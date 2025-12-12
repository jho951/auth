package com.auth.security;

import auth.TokenService;
import com.auth.AuthProperties;
import com.auth.model.Principal;

import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


import java.util.stream.Collectors;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

public final class AuthOncePerRequestFilter extends OncePerRequestFilter {

	private final TokenService tokenService;
	private final AuthProperties props;

	public AuthOncePerRequestFilter(TokenService tokenService, AuthProperties props) {
		this.tokenService = tokenService;
		this.props = props;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) {
		try {
			String auth = request.getHeader("Authorization");
			if (auth != null && auth.startsWith(props.getBearerPrefix())) {
				String token = auth.substring(props.getBearerPrefix().length()).trim();
				Principal p = tokenService.verifyAccessToken(token);

				var authorities = p.getRoles().stream()
					.map(SimpleGrantedAuthority::new)
					.collect(Collectors.toList());

				var authentication = new UsernamePasswordAuthenticationToken(p.getUserId(), null, authorities);
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
			chain.doFilter(request, response);
		} catch (Exception e) {
			SecurityContextHolder.clearContext();
			try { chain.doFilter(request, response); } catch (Exception ignore) {}
		}
	}
}
