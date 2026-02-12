package com.auth.api.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThat;

class ErrorCodeTest {

	@Test
	@DisplayName("ErrorCode의 code()와 defaultMessage()가 정의된 값을 정확히 반환한다.")
	void errorCode_ShouldReturnDefinedValues() {
		ErrorCode code = ErrorCode.INVALID_REQUEST;

		assertThat(code.code()).isEqualTo("A001");
		assertThat(code.defaultMessage()).isEqualTo("잘못된 요청입니다.");
	}

	@Test
	@DisplayName("toString() 호출 시 코드와 기본 메시지가 포함된 문자열을 반환한다.")
	void errorCode_ToStringFormat() {
		ErrorCode code = ErrorCode.BLANK_ACCESS_TOKEN;
		assertThat(code.toString()).isEqualTo("A005 - accessToken이 비었습니다.");
	}

	@ParameterizedTest
	@EnumSource(ErrorCode.class)
	@DisplayName("모든 ErrorCode는 'A'로 시작하는 4자리 코드를 가져야 한다.")
	void errorCode_FormatValidation(ErrorCode errorCode) {
		assertThat(errorCode.code()).startsWith("A").hasSize(4);
	}
}