package com.auth.model;

import com.auth.exception.AuthException;
import com.auth.exception.ErrorCode;

/**
 * 토큰 발급 결과
 * - 웹(스프링)에서는 refresh를 쿠키로 굽거나 바디로 내릴 수 있음
 * - core는 "문자열"만 제공하고, 쿠키 처리(Set-Cookie)는 어댑터(컨트롤러)가 맡는게 범용적
 */
public final class Tokens {

	private final String accessToken;
	private final String refreshToken;

	public Tokens(String accessToken, String refreshToken) {
		if (accessToken == null || accessToken.isBlank()) {
			throw new AuthException(ErrorCode.BLANK_ACCESS_TOKEN, "accessToken must not be blank");
		}
		if (refreshToken == null || refreshToken.isBlank()) {
			throw new AuthException(ErrorCode.BLANK_REFRESH_TOKEN, "refreshToken must not be blank");
		}
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public String getRefreshToken() {
		return refreshToken;
	}
}
