package com.auth.sample.jwt.controller;

import com.auth.api.model.Principal;
import com.auth.api.model.Tokens;
import com.auth.api.model.User;
import com.auth.config.controller.RefreshCookieWriter;
import com.auth.core.service.AuthService;
import com.auth.spi.PasswordVerifier;
import com.auth.spi.UserFinder;
import com.auth.sample.jwt.model.LoginRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api")
public class AuthenticationController {

	private final AuthService authService;
	private final RefreshCookieWriter refreshCookieWriter;
	private final UserFinder userFinder;
	private final PasswordVerifier passwordVerifier;

	public AuthenticationController(
		AuthService authService,
		RefreshCookieWriter refreshCookieWriter,
		UserFinder userFinder,
		PasswordVerifier passwordVerifier
	) {
		this.authService = authService;
		this.refreshCookieWriter = refreshCookieWriter;
		this.userFinder = userFinder;
		this.passwordVerifier = passwordVerifier;
	}

	@PostMapping("/login")
	public ResponseEntity<Tokens> login(@RequestBody LoginRequest request) {
		User user = userFinder.findByUsername(request.username())
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "invalid credentials"));

		if (!passwordVerifier.matches(request.password(), user.getPasswordHash())) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "invalid credentials");
		}

		Principal principal = new Principal(user.getUserId(), user.getRoles());
		Tokens tokens = authService.login(principal);
		return refreshCookieWriter.write(tokens, ResponseEntity.ok(tokens));
	}

	@GetMapping("/profile")
	public Principal profile(@AuthenticationPrincipal Principal principal) {
		if (principal == null) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "missing principal");
		}
		return principal;
	}
}
