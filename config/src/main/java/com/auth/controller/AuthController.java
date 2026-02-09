package com.auth.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.auth.model.Tokens;
import com.auth.AuthProperties;
import com.auth.dto.LoginRequest;
import com.auth.dto.LoginResponse;
import com.auth.service.AuthService;
import com.auth.exception.ErrorCode;
import com.auth.exception.AuthException;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

/**
 * 인증 및 권한 api 컨트롤러입니다.
 * 사용자의 로그인 요청을 처리하여 Access Token과 Refresh Token을 발급하며,
 * 보안을 위해 Refresh Token은 HTTP-Only 쿠키를 통해 관리합니다.
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

	private final AuthService authService;
	private final AuthProperties props;

	public AuthController(AuthService authService, AuthProperties props) {
		this.authService = authService;
		this.props = props;
	}

	/**
	 * 사용자 로그인을 처리합니다.
	 * @param req 사용자 이름(username)과 비밀번호(password)를 담은 요청 객체
	 * @return Access Token을 담은 응답 객체 (Refresh Token은 설정에 따라 쿠키에 포함)
	 */
	@PostMapping("/login")
	public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest req) {
		Tokens tokens = authService.login(req.getUsername(), req.getPassword());
		return withRefreshCookie(tokens, ResponseEntity.ok(new LoginResponse(tokens.getAccessToken())));
	}

	/**
	 * 쿠키에 담긴 Refresh Token을 사용하여 새로운 토큰 쌍을 발급합니다.
	 * @param request 쿠키 정보가 포함된 HTTP 요청 객체
	 * @return 갱신된 Access Token을 담은 응답 객체
	 * @throws AuthException 쿠키에서 Refresh Token을 찾을 수 없는 경우 발생
	 */
	@PostMapping("/refresh")
	public ResponseEntity<LoginResponse> refresh(HttpServletRequest request) {
		String rt = extractRefreshToken(request);
		Tokens tokens = authService.refresh(rt);
		return withRefreshCookie(tokens, ResponseEntity.ok(new LoginResponse(tokens.getAccessToken())));
	}

	/**
	 * 로그아웃을 처리하며 저장된 토큰 정보와 쿠키를 무효화합니다.
	 * @param request 쿠키 정보가 포함된 HTTP 요청 객체
	 * @return 응답 본문이 없는 204 No Content
	 */
	@PostMapping("/logout")
	public ResponseEntity<Void> logout(HttpServletRequest request) {
		String rt = extractRefreshToken(request);
		authService.logout(rt);

		if (!props.isRefreshCookieEnabled()) return ResponseEntity.noContent().build();

		ResponseCookie cleared = ResponseCookie.from(props.getRefreshCookieName(), "")
			.httpOnly(props.isRefreshCookieHttpOnly())
			.secure(props.isRefreshCookieSecure())
			.path(props.getRefreshCookiePath())
			.maxAge(0)
			.sameSite(props.getRefreshCookieSameSite())
			.build();

		return ResponseEntity.noContent()
			.header(HttpHeaders.SET_COOKIE, cleared.toString())
			.build();
	}

	/**
	 * 응답 객체에 Refresh Token 쿠키를 설정하여 반환합니다.
	 * @param tokens 발급된 토큰 세트
	 * @param base 기본 응답 객체 (Access Token 포함)
	 * @return 쿠키 설정이 추가된 ResponseEntity
	 */
	private ResponseEntity<LoginResponse> withRefreshCookie(Tokens tokens, ResponseEntity<LoginResponse> base) {
		if (!props.isRefreshCookieEnabled()) return base;

		ResponseCookie cookie = ResponseCookie.from(props.getRefreshCookieName(), tokens.getRefreshToken())
			.httpOnly(props.isRefreshCookieHttpOnly())
			.secure(props.isRefreshCookieSecure())
			.path(props.getRefreshCookiePath())
			.sameSite(props.getRefreshCookieSameSite())
			.build();

		return ResponseEntity.status(base.getStatusCode())
			.header(HttpHeaders.SET_COOKIE, cookie.toString())
			.body(base.getBody());
	}

	/**
	 * HTTP 요청의 쿠키 목록에서 Refresh Token 값을 추출합니다.
	 * @param request HTTP 요청 객체
	 * @return 쿠키에 저장된 Refresh Token 값
	 * @throws AuthException 설정된 이름의 쿠키가 존재하지 않을 경우 발생
	 */
	private String extractRefreshToken(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if (cookies == null) throw new AuthException(ErrorCode.INVALID_REQUEST, "refresh cookie not found");
		for (Cookie c : cookies) {
			if (props.getRefreshCookieName().equals(c.getName())) return c.getValue();
		}
		throw new AuthException(ErrorCode.INVALID_REQUEST, "refresh cookie not found");
	}
}