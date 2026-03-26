package com.auth.session;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

/**
 * Reads session IDs from HTTP cookies.
 */
public final class DefaultSessionCookieExtractor implements SessionCookieExtractor {

    private final String cookieName;

    public DefaultSessionCookieExtractor(String cookieName) {
        this.cookieName = Objects.requireNonNull(cookieName, "cookieName");
    }

    @Override
    public Optional<String> extract(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null || cookies.length == 0) return Optional.empty();
        return Arrays.stream(cookies)
            .filter(cookie -> cookieName.equals(cookie.getName()))
            .map(Cookie::getValue)
            .findFirst();
    }
}
