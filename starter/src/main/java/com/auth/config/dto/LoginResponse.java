package com.auth.config.dto;

/**
 * 로그인 성공 후 클라이언트에게 반환할 인증 정보를 담는 데이터 전송 객체(DTO)입니다.
 * 인증이 완료된 사용자에게 발급된 Access Token을 포함하며,
 * 클라이언트는 이후 요청 시 이 토큰을 사용하여 권한을 증명합니다.
 */
public class LoginResponse {

	/** 클라이언트가 API 요청 시 인증 헤더에 포함해야 할 Access Token입니다.
	 * 보안을 위해 이 필드는 생성 시점에 한 번만 설정되며 변경할 수 없습니다.
	 */
	private final String accessToken;

	public LoginResponse(String accessToken) {
		this.accessToken = accessToken;
	}
	public String getAccessToken() { return accessToken; }
}