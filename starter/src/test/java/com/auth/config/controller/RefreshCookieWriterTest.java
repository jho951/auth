package com.auth.config.controller;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.auth.api.model.Tokens;
import com.auth.config.AuthProperties;
import com.auth.config.dto.LoginResponse;
import com.auth.config.jwt.AuthJwtProperties;

class RefreshCookieWriterTest {

	@Test
	@DisplayName("refresh cookie가 비활성화면 write는 기존 응답을 그대로 반환한다.")
	void write_ReturnsBase_WhenCookieDisabled() {
		AuthProperties props = cookieProps(false);
		AuthJwtProperties jwtProps = jwtProps(3600);
		RefreshCookieWriter writer = new RefreshCookieWriter(props, jwtProps);
		LoginResponse body = new LoginResponse("access-token");
		ResponseEntity<LoginResponse> base = ResponseEntity.ok(body);

		ResponseEntity<LoginResponse> result = writer.write(new Tokens("access-token", "refresh-token"), base);

		assertThat(result).isSameAs(base);
	}

	@Test
	@DisplayName("refresh cookie가 활성화면 write는 Set-Cookie 헤더를 추가하고 상태/바디를 유지한다.")
	void write_SetsRefreshCookieHeader_WhenCookieEnabled() {
		AuthProperties props = cookieProps(true);
		AuthJwtProperties jwtProps = jwtProps(1234);
		RefreshCookieWriter writer = new RefreshCookieWriter(props, jwtProps);
		LoginResponse body = new LoginResponse("access-token");
		ResponseEntity<LoginResponse> base = ResponseEntity.status(HttpStatus.CREATED).body(body);

		ResponseEntity<LoginResponse> result = writer.write(new Tokens("access-token", "refresh-token"), base);

		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		assertThat(result.getBody()).isSameAs(body);

		String setCookie = result.getHeaders().getFirst(HttpHeaders.SET_COOKIE);
		assertThat(setCookie).isNotBlank();
		assertThat(setCookie).contains("refresh_cookie=refresh-token");
		assertThat(setCookie).contains("Path=/auth");
		assertThat(setCookie).contains("Max-Age=1234");
		assertThat(setCookie).contains("HttpOnly");
		assertThat(setCookie).contains("Secure");
		assertThat(setCookie).contains("SameSite=Strict");
	}

	@Test
	@DisplayName("refresh cookie가 활성화면 clear는 만료 쿠키를 설정한다.")
	void clear_SetsExpiredCookie_WhenCookieEnabled() {
		AuthProperties props = cookieProps(true);
		AuthJwtProperties jwtProps = jwtProps(3600);
		RefreshCookieWriter writer = new RefreshCookieWriter(props, jwtProps);
		ResponseEntity<Void> base = ResponseEntity.noContent().build();

		ResponseEntity<Void> result = writer.clear(base);

		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
		String setCookie = result.getHeaders().getFirst(HttpHeaders.SET_COOKIE);
		assertThat(setCookie).isNotBlank();
		assertThat(setCookie).contains("refresh_cookie=");
		assertThat(setCookie).contains("Path=/auth");
		assertThat(setCookie).contains("Max-Age=0");
		assertThat(setCookie).contains("HttpOnly");
		assertThat(setCookie).contains("Secure");
		assertThat(setCookie).contains("SameSite=Strict");
	}

	@Test
	@DisplayName("refresh cookie가 비활성화면 clear는 기존 응답을 그대로 반환한다.")
	void clear_ReturnsBase_WhenCookieDisabled() {
		AuthProperties props = cookieProps(false);
		AuthJwtProperties jwtProps = jwtProps(3600);
		RefreshCookieWriter writer = new RefreshCookieWriter(props, jwtProps);
		ResponseEntity<Void> base = ResponseEntity.noContent().build();

		ResponseEntity<Void> result = writer.clear(base);

		assertThat(result).isSameAs(base);
	}

	private static AuthProperties cookieProps(boolean enabled) {
		AuthProperties props = new AuthProperties();
		props.setRefreshCookieEnabled(enabled);
		props.setRefreshCookieName("refresh_cookie");
		props.setRefreshCookieHttpOnly(true);
		props.setRefreshCookieSecure(true);
		props.setRefreshCookiePath("/auth");
		props.setRefreshCookieSameSite("Strict");
		return props;
	}

	private static AuthJwtProperties jwtProps(long refreshSeconds) {
		AuthJwtProperties jwtProps = new AuthJwtProperties();
		jwtProps.setRefreshSeconds(refreshSeconds);
		return jwtProps;
	}
}
