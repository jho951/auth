package com.auth.config.dto;

/**
 * 로그인 요청 시 사용자의 인증 정보를 담는 데이터 전송 객체(DTO)입니다.
 * 클라이언트가 로그인을 시도할 때 전달하는 아이디(username)와 비밀번호(password) 정보를 포함
 */
public class LoginRequest {
	/** 사용자가 입력한 식별 ID (예: 이메일, 계정명 등) */
	private String username;

	/** 사용자가 입력한 비밀번호 */
	private String password;

	/** @return 사용자 식별 ID */
	public String getUsername() { return username; }

	/** @return 사용자 비밀번호 */
	public String getPassword() { return password; }
}