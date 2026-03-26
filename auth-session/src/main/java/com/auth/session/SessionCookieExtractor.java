package com.auth.session;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * Helper to read session identifiers from HTTP requests.
 */
public interface SessionCookieExtractor {

    /**
     * Try to resolve a session identifier from the current request.
     */
    Optional<String> extract(HttpServletRequest request);
}
