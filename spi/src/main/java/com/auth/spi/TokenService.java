package com.auth.spi;


import com.auth.api.model.Principal;

/**
 * 토큰 발급/검증 포트.
 * - JWT 구현(nimbus/jjwt 등)은 별도 모듈에서 TokenService 구현체로 제공
 */
public interface TokenService {

	/** Access Token 발급 */
	String issueAccessToken(Principal principal);

	/**
	 * Refresh Token 발급
	 * - "문자열"만 반환 (쿠키로 굽는 건 웹 어댑터 책임)
	 */
	String issueRefreshToken(Principal principal);

	/**
	 * Access Token 검증 -> Principal 추출
	 * @throws RuntimeException if invalid/expired
	 */
	Principal verifyAccessToken(String token);

	/** Refresh Token 검증 -> Principal 추출 */
	Principal verifyRefreshToken(String token);
}

