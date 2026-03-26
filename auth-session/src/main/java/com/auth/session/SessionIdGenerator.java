package com.auth.session;

/**
 * Generates opaque session identifiers.
 */
public interface SessionIdGenerator {

    /**
     * Produce a fresh identifier that can be stored in the {@link SessionStore}.
     */
    String generate();
}
