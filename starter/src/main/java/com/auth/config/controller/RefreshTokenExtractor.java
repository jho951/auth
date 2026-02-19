package com.auth.config.controller;

import com.auth.api.exception.AuthException;
import com.auth.api.exception.ErrorCode;
import com.auth.config.AuthProperties;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

/**
 * HTTP 요청 쿠키에서 Refresh Token 값을 추출합니다.
 */
public class RefreshTokenExtractor {

	private static final String REFRESH_COOKIE_NOT_FOUND = "refresh cookie not found";

	private final AuthProperties props;

	public RefreshTokenExtractor(AuthProperties props) {
		this.props = props;
	}

	public String extract(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if (cookies == null) throw new AuthException(ErrorCode.INVALID_REQUEST, REFRESH_COOKIE_NOT_FOUND);
		for (Cookie c : cookies) {
			if (props.getRefreshCookieName().equals(c.getName())) return c.getValue();
		}
		throw new AuthException(ErrorCode.INVALID_REQUEST, REFRESH_COOKIE_NOT_FOUND);
	}
}
