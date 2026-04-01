package com.auth.config.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;

/** JWT 발급과 검증에 필요한 `auth.jwt.*` 설정을 바인딩하는 프로퍼티 객체입니다. */
@ConfigurationProperties(prefix = "auth.jwt")
public class AuthJwtProperties {

	/** JWT 서명에 사용할 비밀키입니다. */
	private String secret;

	/** Access Token의 유효 기간(초 단위)으로, 기본값은 900초(15분)입니다. */
	private long accessSeconds = 60 * 15;

	/** Refresh Token의 유효 기간(초 단위)으로, 기본값은 1,209,600초(14일)입니다. */
	private long refreshSeconds = 60L * 60 * 24 * 14;

	public String getSecret() { return secret; }
	public void setSecret(String secret) { this.secret = secret; }

	public long getAccessSeconds() { return accessSeconds; }
	public void setAccessSeconds(long accessSeconds) { this.accessSeconds = accessSeconds; }

	public long getRefreshSeconds() { return refreshSeconds; }
	public void setRefreshSeconds(long refreshSeconds) { this.refreshSeconds = refreshSeconds; }
}
