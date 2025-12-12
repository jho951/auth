package com.auth.model;

import java.util.Collections;
import java.util.List;

import com.auth.exception.AuthException;
import com.auth.exception.ErrorCode;

/**
 * 인증된 사용자(요청 주체)
 * - access/refresh 토큰 검증 결과로 만들어짐
 * - 컨트롤러/서비스에서 "현재 사용자"를 표현할 때 사용
 */
public final class Principal {

	private final String userId;
	private final List<String> roles;

	public Principal(String userId, List<String> roles) {
		if (userId == null || userId.isBlank()) {throw new AuthException(ErrorCode.BLANK_USER_ID,"userId must not be blank");}
		this.userId = userId;
		this.roles = roles == null ? Collections.emptyList() : Collections.unmodifiableList(roles);
	}

	public String getUserId() {
		return userId;
	}

	public List<String> getRoles() {
		return roles;
	}

	public boolean hasRole(String role) {
		if (role == null) return false;
		return roles.contains(role);
	}
}
