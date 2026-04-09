package com.auth.api.model;

import java.util.List;
import java.util.Objects;
import java.util.Collections;
import com.auth.common.utils.Strings;

/**
 * 코어가 다루는 "인증용 유저 최소 모델"
 * DB 엔티티(UserEntity)랑 분리
 * 인증/인가에 필요한 정보만 포함
 */
public final class User {
	private final String userId;
	private final String username;
	private final String passwordHash;
	private final List<String> authorities;

	/**
	 * 생성자
	 * @param userId 내부 고유 ID (PK or UUID)
	 * @param username 로그인 식별자(아이디/이메일 등)
	 * @param passwordHash 저장된 해시
	 * @param roles 권한 문자열 (authority 문자열). 호환을 위해 roles 용어를 유지합니다.
	 */
	public User(String userId, String username, String passwordHash, List<String> roles) {
		this.userId = Strings.requireNonBlank(userId, "userId");
		this.username = Strings.requireNonBlank(username, "username");
		this.passwordHash = Strings.requireNonBlank(passwordHash, "passwordHash");
		this.authorities = roles == null ? Collections.emptyList() : List.copyOf(roles);
	}

	public String getUserId() {
		return userId;
	}
	public String getUsername() {
		return username;
	}
	public String getPasswordHash() {
		return passwordHash;
	}
	/** 권한(authority) 문자열 목록입니다. */
	public List<String> getAuthorities() {
		return authorities;
	}

	/**
	 * @deprecated 권한은 roles가 아니라 authorities 용어로 취급합니다. {@link #getAuthorities()}를 사용하세요.
	 */
	@Deprecated
	public List<String> getRoles() {
		return getAuthorities();
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof User)) return false;
		User other = (User) o;
		return Objects.equals(userId, other.userId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(userId);
	}
}
