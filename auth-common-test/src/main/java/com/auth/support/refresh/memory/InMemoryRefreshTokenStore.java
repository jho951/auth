package com.auth.support.refresh.memory;

import java.time.Clock;
import java.time.Instant;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.auth.common.utils.MoreObjects;
import com.auth.spi.RefreshTokenStore;

/**
 * `RefreshTokenStore` 의 메모리 기반 기본 구현체입니다.
 *
 * <p>userId + refreshToken 조합을 키로 저장하며, 만료 시각이 지난 항목은 조회 시점에 정리합니다.
 * 개발 환경이나 간단한 통합 테스트에 적합한 기본 구현입니다.</p>
 */
public final class InMemoryRefreshTokenStore implements RefreshTokenStore {
	private String key(String userId, String refreshToken) {
		return userId + "::" + refreshToken;
	}

	private record Entry(Instant expiresAt) { }

	private final Map<String, Entry> store = new ConcurrentHashMap<>();
	private final Clock clock;

	/** 시스템 UTC 시계를 사용하는 기본 생성자입니다. */
	public InMemoryRefreshTokenStore() {
		this(Clock.systemUTC());
	}

	/**
	 * 테스트 가능한 시간 제어를 위해 외부 Clock 을 주입받는 생성자입니다.
	 */
	public InMemoryRefreshTokenStore(Clock clock) {
		this.clock = MoreObjects.defaultIfNull(clock, Clock.systemUTC());
	}

	@Override
	/** refresh token 과 만료 시각을 메모리에 저장합니다. */
	public void save(String userId, String refreshToken, Instant expiresAt) {
		store.put(key(userId, refreshToken), new Entry(expiresAt));
	}

	@Override
	/**
	 * 저장소 기준으로 refresh token 이 아직 유효한지 확인합니다.
	 *
	 * <p>토큰 자체의 서명과 형식 검증은 `TokenService` 가 담당하고,
	 * 이 메서드는 서버 저장소에 남아 있는지와 만료 시각만 확인합니다.</p>
	 */
	public boolean exists(String userId, String refreshToken) {
		Entry e = store.get(key(userId, refreshToken));
		if (e == null) return false;
		if (Instant.now(clock).isAfter(e.expiresAt)) {
			store.remove(key(userId, refreshToken));
			return false;
		}
		return true;
	}

	@Override
	/** 로그아웃 또는 rotation 이후 이전 refresh token 을 저장소에서 제거합니다. */
	public void revoke(String userId, String refreshToken) {
		store.remove(key(userId, refreshToken));
	}

}
