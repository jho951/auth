package com.auth.api.exception;

/**
 * OSS auth 내부에서 실패 원인을 분류하기 위한 최소 수준의 reason enum 입니다.
 * <p>
 * 이 값은 외부 API 응답 코드 계약을 의미하지 않으며,
 * HTTP 상태 코드나 응답 포맷 매핑은 서비스 애플리케이션이 담당합니다.
 * </p>
 */
public enum AuthFailureReason {
	INVALID_INPUT,
	USER_NOT_FOUND,
	INVALID_CREDENTIALS,
	INVALID_TOKEN,
	REVOKED_TOKEN,
	INTERNAL
}
