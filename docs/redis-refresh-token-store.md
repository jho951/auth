# Redis RefreshTokenStore 가이드

현재 저장소는 Redis 구현을 내장하지 않습니다.
운영 환경에서 refresh token 상태를 서버 기준으로 관리하려면 `RefreshTokenStore`를 직접 구현하면 됩니다.

## 왜 Redis를 쓰는가

- TTL 기반 만료 처리 용이
- 여러 인스턴스 간 공유 가능
- logout / refresh rotation / 강제 만료 처리에 적합

## 구현해야 하는 인터페이스

위치:

- `spi/src/main/java/com/auth/spi/RefreshTokenStore.java`

메서드:

- `save(String userId, String refreshToken, Instant expiresAt)`
- `exists(String userId, String refreshToken)`
- `revoke(String userId, String refreshToken)`

## 간단 예시

```java
@Component
public class RedisRefreshTokenStore implements RefreshTokenStore {

    private final StringRedisTemplate redisTemplate;

    public RedisRefreshTokenStore(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void save(String userId, String refreshToken, Instant expiresAt) {
        String key = key(userId, refreshToken);
        Duration ttl = Duration.between(Instant.now(), expiresAt);
        redisTemplate.opsForValue().set(key, "1", ttl);
    }

    @Override
    public boolean exists(String userId, String refreshToken) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key(userId, refreshToken)));
    }

    @Override
    public void revoke(String userId, String refreshToken) {
        redisTemplate.delete(key(userId, refreshToken));
    }

    private String key(String userId, String refreshToken) {
        return "auth:refresh:" + userId + ":" + refreshToken;
    }
}
```

## 운영 시 고려 사항

- key naming rule
- 한 사용자당 여러 refresh token 허용 여부
- device/session 별 분리 여부
- blacklist 대신 whitelist 방식인지 여부
- Redis 장애 시 fallback 전략

## 현재 저장소와의 관계

기본 `InMemoryRefreshTokenStore`는 `support`에 있고 개발/테스트용입니다.
운영에서는 Redis/DB 구현으로 교체하는 편이 일반적입니다.
