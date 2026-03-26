package com.auth.common.utils;

/** null 처리와 기본값 선택에 사용하는 공통 유틸입니다. */
public final class MoreObjects {
	private MoreObjects() {}

	/** 값이 null 이면 기본값을 반환하고, 아니면 원래 값을 반환합니다. */
	public static <T> T defaultIfNull(T value, T defaultValue) {
		return value == null ? defaultValue : value;
	}
}
