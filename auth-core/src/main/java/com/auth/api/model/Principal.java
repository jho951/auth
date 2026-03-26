package com.auth.api.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.auth.api.exception.AuthException;
import com.auth.api.exception.AuthFailureReason;
import com.auth.common.utils.Strings;

/**
 * Principal은 인증된 주체의 신원을 표현하며, 권한 및 부가 속성을 전달합니다.
 */
public final class Principal implements Serializable {

	private static final long serialVersionUID = 1L;

	private final String userId;
	private final List<String> authorities;
	private final Map<String, Object> attributes;

	public Principal(String userId) {
		this(userId, List.of(), Map.of());
	}

	public Principal(String userId, List<String> roles) {
		this(userId, roles, Map.of());
	}

	public Principal(String userId, Map<String, Object> attributes) {
		this(userId, List.of(), attributes);
	}

	public Principal(String userId, List<String> authorities, Map<String, Object> attributes) {
		if (Strings.isBlank(userId)) {
			throw new AuthException(AuthFailureReason.INVALID_INPUT, "userId must not be blank");
		}
		this.userId = userId;
		this.authorities = authorities == null ? List.of() : List.copyOf(authorities);
		this.attributes = attributes == null ? Map.of() : Map.copyOf(attributes);
	}

	public String getUserId() {
		return userId;
	}

	public List<String> getAuthorities() {
		return authorities;
	}

	/**
	 * @deprecated 역할(roles) 기반 API는 유지하되 authorities와 같은 리스트를 그대로 반환합니다.
	 */
	@Deprecated
	public List<String> getRoles() {
		return getAuthorities();
	}

	public Map<String, Object> getAttributes() {
		return attributes;
	}

	public Object getAttribute(String key) {
		return attributes.get(key);
	}

	@Override
	public String toString() {
		return "Principal(userId=" + userId + ", authorities=" + authorities + ", attributes=" + attributes.keySet() + ")";
	}
}
