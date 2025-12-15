package com.auth.service;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;

import com.auth.exception.AuthException;
import com.auth.exception.ErrorCode;
import com.auth.model.Principal;
import com.auth.model.Tokens;
import com.auth.model.User;

import auth.PasswordVerifier;
import auth.RefreshTokenStore;
import auth.TokenService;
import auth.UserFinder;

/**
 * auth-core의 "유즈케이스(업무 흐름)" 담당 클래스
 * - 스프링/서블릿/HTTP를 모르는 순수 Java
 * - login/refresh/logout만 책임진다.
 */
public final class AuthService {

	private final UserFinder userFinder;
	private final PasswordVerifier passwordVerifier;

	private final TokenService tokenService;
	private final RefreshTokenStore refreshTokenStore;

	private final Duration refreshTtl;
	private final Clock clock;

	public AuthService(
		UserFinder userFinder,
		PasswordVerifier passwordVerifier,
		TokenService tokenService,
		RefreshTokenStore refreshTokenStore,
		Duration refreshTtl,
		Clock clock
	) {
		this.userFinder = requireNonNull(userFinder, "userFinder");
		this.passwordVerifier = requireNonNull(passwordVerifier, "passwordVerifier");
		this.tokenService = requireNonNull(tokenService, "tokenService");
		this.refreshTokenStore = requireNonNull(refreshTokenStore, "refreshTokenStore");
		this.refreshTtl = (refreshTtl == null || refreshTtl.isNegative() || refreshTtl.isZero()) ? Duration.ofDays(14) : refreshTtl;
		this.clock = (clock == null) ? Clock.systemUTC() : clock;
	}

	/** (편의) clock을 주입하지 않는 생성자 */
	public AuthService(
		UserFinder userFinder,
		PasswordVerifier passwordVerifier,
		TokenService tokenService,
		RefreshTokenStore refreshTokenStore,
		Duration refreshTtl
	) {
		this(userFinder, passwordVerifier, tokenService, refreshTokenStore, refreshTtl, Clock.systemUTC());
	}

	/**
	 * 로그인: username/password -> access/refresh 발급 + refresh 저장
	 */
	public Tokens login(String username, String password) {
		if (isBlank(username) || isBlank(password)) {
			throw new AuthException(ErrorCode.INVALID_REQUEST, "username/password must not be blank");
		}

		User user = userFinder.findByUsername(username)
			.orElseThrow(() -> new AuthException(ErrorCode.USER_NOT_FOUND, "user not found"));

		boolean ok = passwordVerifier.matches(password, user.getPasswordHash());
		if (!ok) {throw new AuthException(ErrorCode.INVALID_CREDENTIALS, "invalid credentials");}

		Principal principal = new Principal(user.getUserId(), user.getRoles());

		String access = tokenService.issueAccessToken(principal);
		String refresh = tokenService.issueRefreshToken(principal);

		Instant expiresAt = Instant.now(clock).plus(refreshTtl);
		refreshTokenStore.save(principal.getUserId(), refresh, expiresAt);

		return new Tokens(access, refresh);
	}

	/**
	 * 리프레시: refreshToken -> 새 access/refresh 재발급(rotate)
	 * - 토큰 자체 유효성: TokenService가 검증
	 * - 서버에서 폐기됐는지: RefreshTokenStore가 검증
	 */
	public Tokens refresh(String refreshToken) {
		if (isBlank(refreshToken)) {
			throw new AuthException(ErrorCode.INVALID_REQUEST, "refreshToken must not be blank");
		}

		Principal principal;
		try {
			principal = tokenService.verifyRefreshToken(refreshToken);
		} catch (RuntimeException e) {
			throw new AuthException(ErrorCode.INVALID_TOKEN, "invalid refresh token", e);
		}

		boolean exists = refreshTokenStore.exists(principal.getUserId(), refreshToken);
		if (!exists) {
			throw new AuthException(ErrorCode.TOKEN_REVOKED, "refresh token revoked");
		}

		// rotate(권장): 기존 refresh 폐기 후 새 refresh 발급
		refreshTokenStore.revoke(principal.getUserId(), refreshToken);

		String newAccess = tokenService.issueAccessToken(principal);
		String newRefresh = tokenService.issueRefreshToken(principal);

		Instant expiresAt = Instant.now(clock).plus(refreshTtl);
		refreshTokenStore.save(principal.getUserId(), newRefresh, expiresAt);

		return new Tokens(newAccess, newRefresh);
	}

	/**
	 * 로그아웃: refreshToken을 기준으로 서버 저장소에서 폐기
	 * - access는 보통 짧아서 서버에서 굳이 폐기 리스트를 안 두는 설계가 많음(v1)
	 */
	public void logout(String refreshToken) {
		if (isBlank(refreshToken)) {
			throw new AuthException(ErrorCode.BLANK_REFRESH_TOKEN, "refreshToken must not be blank");
		}

		Principal principal;
		try {
			principal = tokenService.verifyRefreshToken(refreshToken);
		} catch (RuntimeException e) {
			throw new AuthException(ErrorCode.INVALID_TOKEN, "invalid refresh token", e);
		}

		refreshTokenStore.revoke(principal.getUserId(), refreshToken);
	}

	private static boolean isBlank(String s) {
		return s == null || s.isBlank();
	}

	private static <T> T requireNonNull(T v, String name) {
		if (v == null) throw new IllegalArgumentException(name + " must not be null");
		return v;
	}
}
