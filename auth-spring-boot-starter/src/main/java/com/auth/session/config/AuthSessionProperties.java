package com.auth.session.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

/** Session starter configuration properties. */
@ConfigurationProperties(prefix = "auth.session")
public class AuthSessionProperties {

    private String cookieName = "AUTH_SESSION";
    private Duration ttl = Duration.ofHours(1);

    public String getCookieName() {
        return cookieName;
    }
    public void setCookieName(String cookieName) {
        this.cookieName = cookieName;
    }
    public Duration getTtl() {
        return ttl;
    }
    public void setTtl(Duration ttl) {
        this.ttl = ttl;
    }
}
