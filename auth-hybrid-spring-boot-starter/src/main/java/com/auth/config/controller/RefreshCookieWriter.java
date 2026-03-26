package com.auth.config.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;

import com.auth.api.model.Tokens;
import com.auth.config.AuthProperties;

/** 응답 본문을 유지한 채 Refresh Token 쿠키만 추가하거나 제거 */
public class RefreshCookieWriter {

	private final AuthProperties props;
	private final long refreshSeconds;

	/** refresh cookie 설정과 수명 설정을 주입받습니다. */
	public RefreshCookieWriter(AuthProperties props, long refreshSeconds) {
		this.props = props;
		this.refreshSeconds = refreshSeconds;
	}

	public <T> ResponseEntity<T> write(Tokens tokens, ResponseEntity<T> base) {
		if (!props.isRefreshCookieEnabled()) return base;

		ResponseCookie cookie = ResponseCookie.from(props.getRefreshCookieName(), tokens.getRefreshToken())
			.httpOnly(props.isRefreshCookieHttpOnly())
			.secure(props.isRefreshCookieSecure())
			.path(props.getRefreshCookiePath())
			.maxAge(refreshSeconds)
			.sameSite(props.getRefreshCookieSameSite())
			.build();

		return ResponseEntity.status(base.getStatusCode())
			.header(HttpHeaders.SET_COOKIE, cookie.toString())
			.body(base.getBody());
	}

	public ResponseEntity<Void> clear(ResponseEntity<Void> base) {
		if (!props.isRefreshCookieEnabled()) return base;

		ResponseCookie cleared = ResponseCookie.from(props.getRefreshCookieName(), "")
			.httpOnly(props.isRefreshCookieHttpOnly())
			.secure(props.isRefreshCookieSecure())
			.path(props.getRefreshCookiePath())
			.maxAge(0)
			.sameSite(props.getRefreshCookieSameSite())
			.build();

		return ResponseEntity.status(base.getStatusCode())
			.header(HttpHeaders.SET_COOKIE, cleared.toString())
			.build();
	}
}
