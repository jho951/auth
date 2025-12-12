package com.auth.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.auth.AuthProperties;
import com.auth.dto.LoginRequest;
import com.auth.dto.LoginResponse;
import com.auth.exception.AuthException;
import com.auth.exception.ErrorCode;
import com.auth.model.Tokens;
import com.auth.service.AuthService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/auth")
public class AuthController {

	private final AuthService authService;
	private final AuthProperties props;

	public AuthController(AuthService authService, AuthProperties props) {
		this.authService = authService;
		this.props = props;
	}

	@PostMapping("/login")
	public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest req) {
		Tokens tokens = authService.login(req.getUsername(), req.getPassword());
		return withRefreshCookie(tokens, ResponseEntity.ok(new LoginResponse(tokens.getAccessToken())));
	}

	@PostMapping("/refresh")
	public ResponseEntity<LoginResponse> refresh(HttpServletRequest request) {
		String rt = extractRefreshToken(request);
		Tokens tokens = authService.refresh(rt);
		return withRefreshCookie(tokens, ResponseEntity.ok(new LoginResponse(tokens.getAccessToken())));
	}

	@PostMapping("/logout")
	public ResponseEntity<Void> logout(HttpServletRequest request) {
		String rt = extractRefreshToken(request);
		authService.logout(rt);

		if (!props.isRefreshCookieEnabled()) return ResponseEntity.noContent().build();

		ResponseCookie cleared = ResponseCookie.from(props.getRefreshCookieName(), "")
			.httpOnly(props.isRefreshCookieHttpOnly())
			.secure(props.isRefreshCookieSecure())
			.path(props.getRefreshCookiePath())
			.maxAge(0)
			.sameSite(props.getRefreshCookieSameSite())
			.build();

		return ResponseEntity.noContent()
			.header(HttpHeaders.SET_COOKIE, cleared.toString())
			.build();
	}

	private ResponseEntity<LoginResponse> withRefreshCookie(Tokens tokens, ResponseEntity<LoginResponse> base) {
		if (!props.isRefreshCookieEnabled()) return base;

		ResponseCookie cookie = ResponseCookie.from(props.getRefreshCookieName(), tokens.getRefreshToken())
			.httpOnly(props.isRefreshCookieHttpOnly())
			.secure(props.isRefreshCookieSecure())
			.path(props.getRefreshCookiePath())
			.sameSite(props.getRefreshCookieSameSite())
			.build();

		return ResponseEntity.status(base.getStatusCode())
			.header(HttpHeaders.SET_COOKIE, cookie.toString())
			.body(base.getBody());
	}

	private String extractRefreshToken(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if (cookies == null) throw new AuthException(ErrorCode.INVALID_REQUEST, "refresh cookie not found");
		for (Cookie c : cookies) {
			if (props.getRefreshCookieName().equals(c.getName())) return c.getValue();
		}
		throw new AuthException(ErrorCode.INVALID_REQUEST, "refresh cookie not found");
	}
}
