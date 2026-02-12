package com.auth.api.model;

import java.util.List;
import java.util.Objects;
import java.util.Collections;
import com.auth.common.utils.Strings;

/**
 * 코어가 다루는 "인증용 유저 최소 모델"
 * - DB 엔티티(UserEntity)랑 분리
 * - 인증/인가에 필요한 정보만 포함
 */
public final class User {
	// 내부 고유 ID (PK or UUID)
	private final String userId;
	// 로그인 식별자(아이디/이메일 등)
	private final String username;
	// 저장된 해시
	private final String passwordHash;
	// 권한 문자열
	private final List<String> roles;

	public User(String userId, String username, String passwordHash, List<String> roles) {
		this.userId = Strings.requireNonBlank(userId, "userId");
		this.username = Strings.requireNonBlank(username, "username");
		this.passwordHash = Strings.requireNonBlank(passwordHash, "passwordHash");
		this.roles = roles == null ? Collections.emptyList() : Collections.unmodifiableList(roles);
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
	public List<String> getRoles() {
		return roles;
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
