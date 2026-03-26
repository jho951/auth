package com.auth.hybrid;

import java.util.Optional;

/**
 * Value object that carries JWT and session identifiers for hybrid authentication.
 */
public record HybridAuthenticationContext(Optional<String> accessToken, Optional<String> sessionId) {

    public static HybridAuthenticationContext of(String accessToken, String sessionId) {
        return new HybridAuthenticationContext(Optional.ofNullable(accessToken), Optional.ofNullable(sessionId));
    }
}
