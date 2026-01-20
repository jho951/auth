package com.auth.model;

import com.auth.model.Principal;
import com.auth.exception.AuthException;
import com.auth.exception.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class PrincipalTest {

	@Test
	@DisplayName("사용자 ID와 권한 목록으로 Principal 객체를 생성한다.")
	void createPrincipal_Success() {
		Principal principal = new Principal("user-1", List.of("ROLE_USER", "ROLE_ADMIN"));

		assertThat(principal.getUserId()).isEqualTo("user-1");
		assertThat(principal.getRoles()).hasSize(2).contains("ROLE_USER", "ROLE_ADMIN");
	}

	@Test
	@DisplayName("roles가 null이면 빈 리스트로 초기화된다.")
	void createPrincipal_NullRoles_EmptyList() {
		Principal principal = new Principal("user-1", null);
		assertThat(principal.getRoles()).isEmpty();
	}

	@Test
	@DisplayName("hasRole은 특정 권한 보유 여부를 정확히 판단한다.")
	void hasRole_Check() {
		Principal principal = new Principal("user-1", List.of("ROLE_USER"));

		assertThat(principal.hasRole("ROLE_USER")).isTrue();
		assertThat(principal.hasRole("ROLE_ADMIN")).isFalse();
		assertThat(principal.hasRole(null)).isFalse();
	}

	@Test
	@DisplayName("반환된 권한 리스트를 외부에서 수정하려고 하면 예외가 발생한다 (불변성).")
	void getRoles_IsUnmodifiable() {
		Principal principal = new Principal("user-1", List.of("ROLE_USER"));
		List<String> roles = principal.getRoles();

		assertThatThrownBy(() -> roles.add("NEW_ROLE"))
			.isInstanceOf(UnsupportedOperationException.class);
	}
}