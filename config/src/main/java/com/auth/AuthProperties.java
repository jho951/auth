package com.auth;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "auth")
public class AuthProperties {

	// 기본 /auth API 제공 여부
	private boolean endpointsEnabled = true;

	// Bearer prefix
	private String bearerPrefix = "Bearer ";

	// refresh cookie 옵션
	private boolean refreshCookieEnabled = true;
	private String refreshCookieName = "refresh_token";
	private boolean refreshCookieHttpOnly = true;
	private boolean refreshCookieSecure = true;
	private String refreshCookiePath = "/";
	private String refreshCookieSameSite = "Lax";

	// (호스트가 이미 SecurityConfig를 가지고 있으면 자동 구성 안 함)
	private boolean autoSecurity = true;

	public boolean isEndpointsEnabled() { return endpointsEnabled; }
	public void setEndpointsEnabled(boolean endpointsEnabled) { this.endpointsEnabled = endpointsEnabled; }

	public String getBearerPrefix() { return bearerPrefix; }
	public void setBearerPrefix(String bearerPrefix) { this.bearerPrefix = bearerPrefix; }

	public boolean isRefreshCookieEnabled() { return refreshCookieEnabled; }
	public void setRefreshCookieEnabled(boolean refreshCookieEnabled) { this.refreshCookieEnabled = refreshCookieEnabled; }

	public String getRefreshCookieName() { return refreshCookieName; }
	public void setRefreshCookieName(String refreshCookieName) { this.refreshCookieName = refreshCookieName; }

	public boolean isRefreshCookieHttpOnly() { return refreshCookieHttpOnly; }
	public void setRefreshCookieHttpOnly(boolean refreshCookieHttpOnly) { this.refreshCookieHttpOnly = refreshCookieHttpOnly; }

	public boolean isRefreshCookieSecure() { return refreshCookieSecure; }
	public void setRefreshCookieSecure(boolean refreshCookieSecure) { this.refreshCookieSecure = refreshCookieSecure; }

	public String getRefreshCookiePath() { return refreshCookiePath; }
	public void setRefreshCookiePath(String refreshCookiePath) { this.refreshCookiePath = refreshCookiePath; }

	public String getRefreshCookieSameSite() { return refreshCookieSameSite; }
	public void setRefreshCookieSameSite(String refreshCookieSameSite) { this.refreshCookieSameSite = refreshCookieSameSite; }

	public boolean isAutoSecurity() { return autoSecurity; }
	public void setAutoSecurity(boolean autoSecurity) { this.autoSecurity = autoSecurity; }
}
