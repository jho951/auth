package com.auth.api.model;

import java.util.List;
import java.util.Collections;

import com.auth.api.exception.ErrorCode;
import com.auth.api.exception.AuthException;
import com.auth.common.utils.Strings;

/**
 * 로그인 시: 서버가 유저에게 AccessToken을 발급함.
 * API 호출 시: 유저가 AccessToken과 함께 요청을 보냄.
 * 서버 내부: * TokenProvider: "음, 이 토큰 진짜네? 아이디는 'userA'고 권한은 'USER'군."
 * Principal 생성: new Principal("userA", List.of("USER"))
 * SecurityContext: 생성된 Principal을 보관함.
 * 컨트롤러/서비스: 보관된 Principal을 꺼내서 "아, 지금 요청한 사람이 'userA'구나!"라고 확인하며 비즈니스 로직 수행.
 * AccessToken은 "암호화된 문자열"일 뿐이라서, 서비스 로직(비즈니스 로직)에서 매번 그 안의 내용을 파싱해서 쓰기에는 너무 번거롭고 성능상 비효율적입니다.
 * <h1></h1>
 * <ul>
 * <li>유효한 액세스 토큰의 클레임(Claim)을 바탕으로 생성됨</li>
 * <li>캡슐화를 통해 </li>
 * <li>컨트롤러 / 서비스에서 "현재 사용자"를 표현할 때 사용</li>
 * </ul>
 */
public final class Principal {

	/** 사용자 식별 고유 키 **/
	private final String userId;
	/** 사용자 권한 ex) USER, ADMIN */
	private final List<String> roles;

	public Principal(String userId, List<String> roles) {
		if (Strings.isBlank(userId)) {throw new AuthException(ErrorCode.BLANK_USER_ID, "userId must not be blank");}
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
		return role != null && roles.contains(role);
	}
}
