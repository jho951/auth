package com.auth.hybrid;

import com.auth.api.model.Principal;
import java.util.Optional;

/**
 * A strategy that may authenticate a user using JWT, session, or both.
 */
public interface HybridAuthenticationProvider {

    /**
     * Try to authenticate the provided context and return the resolved principal.
     */
    Optional<Principal> authenticate(HybridAuthenticationContext context);
}
