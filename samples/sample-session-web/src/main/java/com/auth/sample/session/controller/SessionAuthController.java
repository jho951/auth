package com.auth.sample.session.controller;

import java.time.Duration;

import com.auth.api.model.Principal;
import com.auth.api.model.User;
import com.auth.sample.session.model.LoginRequest;
import com.auth.session.SessionCookieExtractor;
import com.auth.session.SessionService;
import com.auth.session.config.AuthSessionProperties;
import com.auth.spi.PasswordVerifier;
import com.auth.spi.UserFinder;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/session")
public class SessionAuthController {

	private final PasswordVerifier passwordVerifier;
	private final UserFinder userFinder;
	private final SessionService sessionService;
	private final SessionCookieExtractor sessionCookieExtractor;
	private final AuthSessionProperties sessionProperties;

	public SessionAuthController(
		PasswordVerifier passwordVerifier,
		UserFinder userFinder,
		SessionService sessionService,
		SessionCookieExtractor sessionCookieExtractor,
		AuthSessionProperties sessionProperties
	) {
		this.passwordVerifier = passwordVerifier;
		this.userFinder = userFinder;
		this.sessionService = sessionService;
		this.sessionCookieExtractor = sessionCookieExtractor;
		this.sessionProperties = sessionProperties;
	}

	@PostMapping("/login")
	public ResponseEntity<Void> login(@RequestBody LoginRequest request) {
		User user = userFinder.findByUsername(request.username())
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "invalid credentials"));

		if (!passwordVerifier.matches(request.password(), user.getPasswordHash())) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "invalid credentials");
		}

		Principal principal = new Principal(user.getUserId(), user.getRoles());
		String sessionId = sessionService.create(principal);

		return ResponseEntity.ok()
			.header(HttpHeaders.SET_COOKIE, createCookie(sessionId, sessionProperties.getTtl(), false).toString())
			.build();
	}

	@GetMapping("/me")
	public Principal me(@AuthenticationPrincipal Principal principal) {
		if (principal == null) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "unauthenticated");
		}
		return principal;
	}

	@PostMapping("/logout")
	public ResponseEntity<Void> logout(HttpServletRequest request) {
		sessionCookieExtractor.extract(request).ifPresent(sessionService::revoke);
		return ResponseEntity.ok()
			.header(HttpHeaders.SET_COOKIE, createCookie("", Duration.ZERO, true).toString())
			.build();
	}

	private ResponseCookie createCookie(String value, Duration ttl, boolean clearing) {
		long maxAge = clearing ? 0 : Math.max(0, ttl.getSeconds());
		return ResponseCookie.from(sessionProperties.getCookieName(), value)
			.httpOnly(true)
			.path("/")
			.maxAge(maxAge)
			.build();
	}
}
