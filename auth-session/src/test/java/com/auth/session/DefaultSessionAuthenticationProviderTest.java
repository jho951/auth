package com.auth.session;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.auth.api.model.Principal;

class DefaultSessionAuthenticationProviderTest {

	@Test
	@DisplayName("존재하는 세션 ID는 Principal을 반환한다")
	void authenticateReturnsPrincipal() {
		SessionStore store = new SimpleSessionStore();
		SessionPrincipalMapper mapper = new IdentitySessionPrincipalMapper();
		SessionAuthenticationProvider provider = new DefaultSessionAuthenticationProvider(store, mapper);

		Principal principal = principal("session-user");
		store.save("session-id", principal);

		assertThat(provider.authenticate("session-id")).contains(principal);
	}

	@Test
	@DisplayName("존재하지 않는 세션은 빈 Optional을 반환한다")
	void authenticateEmptyWhenMissing() {
		SessionStore store = new SimpleSessionStore();
		SessionPrincipalMapper mapper = new IdentitySessionPrincipalMapper();
		SessionAuthenticationProvider provider = new DefaultSessionAuthenticationProvider(store, mapper);

		assertThat(provider.authenticate("missing")).isEmpty();
	}

	private static Principal principal(String userId) {
		return new Principal(userId);
	}
}
