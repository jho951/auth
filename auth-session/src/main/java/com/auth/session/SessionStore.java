package com.auth.session;

import com.auth.api.model.Principal;
import java.util.Optional;

/** Repository for session identifiers mapped to authenticated principals. */
public interface SessionStore {

    /** Store a mapping between a session ID and a principal. */
    void save(String sessionId, Principal principal);

    /** Lookup a principal by session ID. */
    Optional<Principal> find(String sessionId);

    /** Invalidate the provided session ID. */
    void revoke(String sessionId);
}
