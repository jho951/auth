package com.auth.api.model;

import com.auth.api.exception.AuthException;
import com.auth.api.exception.AuthFailureReason;
import com.auth.common.utils.Strings;

/**
 * 토큰 발급 결과
 * 웹(스프링)에서는 refresh를 쿠키로 굽거나 바디로 내릴 수 있음
 * core는 "문자열"만 제공하고, 쿠키 처리는 컨트롤러가 담당
 */
public final class Tokens {
	private final String accessToken;
	private final String refreshToken;

	/**
	 * 생성자
	 * @param accessToken 토큰
	 * @param refreshToken 재발급 토큰
	 */
	public Tokens(String accessToken, String refreshToken) {
		if (Strings.isBlank(accessToken)) throw new AuthException(AuthFailureReason.INVALID_INPUT, "accessToken must not be blank");
		if (Strings.isBlank(refreshToken)) throw new AuthException(AuthFailureReason.INVALID_INPUT, "refreshToken must not be blank");
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
