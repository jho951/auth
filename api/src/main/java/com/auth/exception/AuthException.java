package com.auth.exception;

public class AuthException extends RuntimeException {

	private final ErrorCode errorCode;

	public AuthException(ErrorCode errorCode) {
		super(errorCode.defaultMessage());
		this.errorCode = errorCode;
	}

	public AuthException(ErrorCode errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
	}

	public AuthException(ErrorCode errorCode, String message, Throwable cause) {
		super(message, cause);
		this.errorCode = errorCode;
	}

	public AuthException(ErrorCode errorCode, Throwable cause) {
		super(errorCode.defaultMessage(), cause);
		this.errorCode = errorCode;
	}

	public ErrorCode getErrorCode() {
		return errorCode;
	}

	@Override
	public String toString() {
		return "AuthException{" + "errorCode=" + errorCode.code() + ", message=" + getMessage() + '}';
	}
}
