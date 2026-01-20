package com.auth.exception;

import com.auth.exception.AuthException;
import com.auth.exception.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AuthExceptionTest {

	@Test
	@DisplayName("ErrorCode만 인자로 전달할 경우, 해당 코드의 기본 메시지를 예외 메시지로 사용한다.")
	void authException_ConstructorWithOnlyErrorCode() {
		ErrorCode code = ErrorCode.TOKEN_EXPIRED;

		AuthException exception = new AuthException(code);

		assertThat(exception.getMessage()).isEqualTo(code.defaultMessage());
		assertThat(exception.getErrorCode()).isEqualTo(code);
	}

	@Test
	@DisplayName("사용자 정의 메시지를 함께 전달할 경우, 기본 메시지 대신 전달된 메시지를 사용한다.")
	void authException_ConstructorWithCustomMessage() {
		// given
		ErrorCode code = ErrorCode.INVALID_TOKEN;
		String customMessage = "토큰의 서명이 유효하지 않습니다.";

		// when
		AuthException exception = new AuthException(code, customMessage);

		// then
		assertThat(exception.getMessage()).isEqualTo(customMessage);
		assertThat(exception.getErrorCode()).isEqualTo(code);
	}

	@Test
	@DisplayName("원인 예외(Throwable cause)가 포함되어도 ErrorCode 정보를 유지한다.")
	void authException_WithCause() {
		RuntimeException cause = new RuntimeException("Original error");
		AuthException exception = new AuthException(ErrorCode.INTERNAL_ERROR, cause);

		assertThat(exception.getCause()).isEqualTo(cause);
		assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.INTERNAL_ERROR);
	}

	@Test
	@DisplayName("toString() 호출 시 ErrorCode의 심볼릭 코드와 메시지를 포함한다.")
	void authException_ToString() {
		AuthException exception = new AuthException(ErrorCode.USER_NOT_FOUND, "유저를 찾을 수 없음");

		assertThat(exception.toString())
			.contains("A002")
			.contains("유저를 찾을 수 없음");
	}
}