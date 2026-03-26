package com.auth.session;

import com.auth.api.model.Principal;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A basic in-memory {@link SessionStore} meant for early-session support.
 */
public final class SimpleSessionStore implements SessionStore {

    private final Map<String, Principal> sessions = new ConcurrentHashMap<>();

    @Override
    public void save(String sessionId, Principal principal) {
        Objects.requireNonNull(sessionId, "sessionId");
        Objects.requireNonNull(principal, "principal");
        sessions.put(sessionId, principal);
    }

    @Override
    public Optional<Principal> find(String sessionId) {
        return Optional.ofNullable(sessions.get(sessionId));
    }

    @Override
    public void revoke(String sessionId) {
        sessions.remove(sessionId);
    }
}
