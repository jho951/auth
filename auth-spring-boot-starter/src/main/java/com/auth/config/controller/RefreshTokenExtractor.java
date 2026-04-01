package com.auth.config.controller;

import com.auth.api.exception.AuthException;
import com.auth.api.exception.AuthFailureReason;
import com.auth.config.AuthProperties;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

/**
 * HTTP 요청 쿠키에서 Refresh Token 값을 읽어오는 웹 도우미입니다.
 */
public class RefreshTokenExtractor {

	private static final String REFRESH_COOKIE_NOT_FOUND = "refresh cookie not found";
	private final AuthProperties props;

	/** refresh cookie 이름 설정을 주입받습니다. */
	public RefreshTokenExtractor(AuthProperties props) {
		this.props = props;
	}

	/** 요청 쿠키 중 설정된 refresh cookie 값을 반환합니다. */
	public String extract(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if (cookies == null) throw new AuthException(AuthFailureReason.INVALID_INPUT, REFRESH_COOKIE_NOT_FOUND);
		for (Cookie c : cookies) {
			if (props.getRefreshCookieName().equals(c.getName())) return c.getValue();
		}
		throw new AuthException(AuthFailureReason.INVALID_INPUT, REFRESH_COOKIE_NOT_FOUND);
	}
}
