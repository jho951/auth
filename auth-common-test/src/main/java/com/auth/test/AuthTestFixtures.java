package com.auth.test;

import com.auth.api.model.Principal;

/** Shared fixtures for auth module tests. */
public final class AuthTestFixtures {

	private AuthTestFixtures() {}

	/** * 오직 식별자(ID)만 가진 Principal을 생성합니다.
	 * 권한(Role)은 이 모듈의 관심사가 아니므로 포함하지 않습니다.
	 */
	public static Principal principal(String userId) {
		return new Principal(userId);
	}
}
