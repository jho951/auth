package com.auth.support.refresh.memory;

import java.time.Clock;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.auth.spi.RefreshTokenStore;

/** 개발과 기본 통합용 메모리 기반 RefreshTokenStore 구현체입니다. */
public final class InMemoryRefreshTokenStore implements RefreshTokenStore {

	private final Map<String, Entry> store = new ConcurrentHashMap<>();
	private final Clock clock;

	public InMemoryRefreshTokenStore() {
		this(Clock.systemUTC());
	}

	public InMemoryRefreshTokenStore(Clock clock) {
		this.clock = clock;
	}

	@Override
	public void save(String userId, String refreshToken, Instant expiresAt) {
		store.put(key(userId, refreshToken), new Entry(expiresAt));
	}

	@Override
	public boolean exists(String userId, String refreshToken) {
		Entry e = store.get(key(userId, refreshToken));
		if (e == null) {
			return false;
		}
		if (Instant.now(clock).isAfter(e.expiresAt)) {
			store.remove(key(userId, refreshToken));
			return false;
		}
		return true;
	}

	@Override
	public void revoke(String userId, String refreshToken) {
		store.remove(key(userId, refreshToken));
	}

	private String key(String userId, String refreshToken) {
		return userId + "::" + refreshToken;
	}

	private record Entry(Instant expiresAt) {
	}
}
