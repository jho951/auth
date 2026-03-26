package com.auth.session;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.auth.api.model.Principal;
import com.auth.test.AuthTestFixtures;

class SessionServiceTest {

	private final SessionStore store = new SimpleSessionStore();
	private final SessionIdGenerator generator = () -> "session-42";
	private final SessionService service = new SessionService(store, generator, Duration.ofMinutes(30));

	@Test
	@DisplayName("세션을 생성하면 고정된 식별자로 저장된다")
	void createSessionStoresPrincipal() {
		String sessionId = service.create(AuthTestFixtures.principal("user-1"));

		assertThat(sessionId).isEqualTo("session-42");
		assertThat(service.resolve(sessionId))
			.map(Principal::getUserId)
			.contains("user-1");
	}

	@Test
	@DisplayName("세션을 폐기하면 더 이상 조회되지 않는다")
	void revokeSessionRemovesPrincipal() {
		String sessionId = service.create(AuthTestFixtures.principal("user-1"));
		service.revoke(sessionId);

		assertThat(service.resolve(sessionId)).isEmpty();
	}
}
