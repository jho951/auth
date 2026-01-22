package com.auth.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * JWT 설정
 * prefix: auth.jwt
 */
@ConfigurationProperties(prefix = "auth.jwt")
public class AuthJwtProperties {

    /**
     * HS256 secret (최소 32바이트 권장)
     */
    private String secret;

    /** access token TTL (seconds) */
    private long accessSeconds = 60 * 15;

    /** refresh token TTL (seconds) */
    private long refreshSeconds = 60L * 60 * 24 * 14;

    public String getSecret() { return secret; }
    public void setSecret(String secret) { this.secret = secret; }

    public long getAccessSeconds() { return accessSeconds; }
    public void setAccessSeconds(long accessSeconds) { this.accessSeconds = accessSeconds; }

    public long getRefreshSeconds() { return refreshSeconds; }
    public void setRefreshSeconds(long refreshSeconds) { this.refreshSeconds = refreshSeconds; }
}
