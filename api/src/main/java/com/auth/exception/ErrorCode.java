package com.auth.exception;

/**
 * 프로젝트에서 사용하는 에러 코드
 * 해당 코드로 HTTP 상태코드 매핑
 */
public enum ErrorCode {

	// 입력
	INVALID_REQUEST("A001", "잘못된 요청입니다."),

	// 로그인
	USER_NOT_FOUND("A002", "잘못된 요청입니다."),
	INVALID_CREDENTIALS("A003", "잘못된 요청입니다."),

	// 토큰
	INVALID_TOKEN("A004", "잘못된 요청입니다."),
	BLANK_ACCESS_TOKEN("A005","accessToken이 비었습니다."),
	BLANK_REFRESH_TOKEN("A006","refreshToken이 비었습니다."),
	TOKEN_REVOKED("A007", "서버 저장소 기준으로 폐기된 refresh"),
	TOKEN_EXPIRED("A008", "TokenService가 만료를 구분해서 던질 때 사용"),
	BLANK_USER_ID("A009","userId가 비어있습니다."),

	// 기타
	INTERNAL_ERROR("A010", "잘못된 요청입니다.");

	private final String code;
	private final String defaultMessage;

	ErrorCode(String code, String defaultMessage) {
		this.code = code;
		this.defaultMessage = defaultMessage;
	}

	/** "E001", "E010" 같은 심볼 코드 */
	public String code() {
		return code;
	}

	/** 기본 메시지. 로그용/디폴트 응답용 */
	public String defaultMessage() {
		return defaultMessage;
	}

	@Override
	public String toString() {
		return code + " - " + defaultMessage;
	}
}