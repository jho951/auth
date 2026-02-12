package com.auth.spi;

import java.time.Instant;

/**
 * Refresh Token 저장소 포트.
 * - 구현: Redis/DB/인메모리/파일 등
 * - v1: refreshToken 그대로 저장해도 됨(간단)
 * - v2: tokenHash, jti(토큰ID) 기반 저장으로 강화 가능
 */
public interface RefreshTokenStore {

	/**
	 * refreshToken을 저장한다.
	 * - userId별로 "현재 유효한 refresh"를 1개로 제한할지,
	 *   여러 개를 허용할지는 구현체 정책으로 결정 가능
	 */
	void save(String userId, String refreshToken, Instant expiresAt);

	/**
	 * refreshToken이 저장소 기준으로 유효한지 확인한다.
	 * - 토큰 자체의 서명/만료는 TokenService가 검증
	 * - 여긴 "서버에서 폐기된 토큰인지"를 확인하는 용도
	 */
	boolean exists(String userId, String refreshToken);

	/**
	 * 로그아웃/강제 만료 시 폐기
	 */
	void revoke(String userId, String refreshToken);
}
