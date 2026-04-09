package com.auth.session;

import com.auth.api.model.Principal;
import java.util.Collections;
import java.util.Optional;
import java.util.Objects;

/** Default {@link SessionAuthenticationProvider} that uses a {@link SessionStore} and mapper. */
public final class DefaultSessionAuthenticationProvider implements SessionAuthenticationProvider {

    private final SessionStore sessionStore;
    private final SessionPrincipalMapper principalMapper;

	/**
	 *
	 * @param sessionStore
	 * @param principalMapper
	 */
    public DefaultSessionAuthenticationProvider(SessionStore sessionStore, SessionPrincipalMapper principalMapper) {
        this.sessionStore = Objects.requireNonNull(sessionStore, "sessionStore");
        this.principalMapper = Objects.requireNonNull(principalMapper, "principalMapper");
    }

    @Override
    public Optional<Principal> authenticate(String sessionId) {
        if (sessionId == null ) return Optional.empty();
		if (sessionId.isBlank()) return Optional.empty();
        return sessionStore.find(sessionId).map(existing -> principalMapper.map(sessionId, existing, Collections.emptyMap()));
    }
}
