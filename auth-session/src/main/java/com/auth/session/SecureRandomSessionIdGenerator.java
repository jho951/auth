package com.auth.session;

import java.security.SecureRandom;
import java.util.Base64;

/** Default {@link SessionIdGenerator} that uses URL-safe Base64 strings. */
public final class SecureRandomSessionIdGenerator implements SessionIdGenerator {

    private static final int DEFAULT_BYTES = 24;
    private final SecureRandom random = new SecureRandom();

    @Override
    public String generate() {
        byte[] bytes = new byte[DEFAULT_BYTES];
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}
