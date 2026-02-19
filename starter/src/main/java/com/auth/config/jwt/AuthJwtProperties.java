package com.auth.config.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * JWT(JSON Web Token) 생성 및 검증에 필요한 설정 정보를 관리하는 클래스입니다.
 * application.yml이나 application.properties에 적힌 텍스트 정보를 자바 객체로 옮겨 담는 역할을 합니다.
 */
@ConfigurationProperties(prefix = "auth.jwt")
public class AuthJwtProperties {

	/** JWT 서명에 사용할 비밀키입니다. 보안을 위해 HS256 알고리즘 기준 최소 32바이트 이상의 문자열을 권장합니다. */
	private String secret;

	/** Access Token의 유효 기간(초 단위)입니다. 기본값은 900초(15분)입니다. */
	private long accessSeconds = 60 * 15;

	/** Refresh Token의 유효 기간(초 단위)입니다.기본값은 1,209,600초(14일)입니다. */
	private long refreshSeconds = 60L * 60 * 24 * 14;

	public String getSecret() { return secret; }
	public void setSecret(String secret) { this.secret = secret; }

	public long getAccessSeconds() { return accessSeconds; }
	public void setAccessSeconds(long accessSeconds) { this.accessSeconds = accessSeconds; }

	public long getRefreshSeconds() { return refreshSeconds; }
	public void setRefreshSeconds(long refreshSeconds) { this.refreshSeconds = refreshSeconds; }
}