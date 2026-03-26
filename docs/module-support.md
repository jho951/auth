# Module: support

## 역할

`support`는 `spi`의 **기본 구현체**를 제공합니다.

## 포함 클래스

- `com.auth.support.jwt.JwtTokenService`
- `com.auth.support.password.bcrypt.BCryptPasswordVerifier`
- `com.auth.support.refresh.memory.InMemoryRefreshTokenStore`

## 클래스별 설명

### `JwtTokenService`

기본 `TokenService` 구현입니다.

- HS256 기반 서명
- access/refresh token 발급
- `roles`, `token_type` claim 기록
- access/refresh 검증 후 `Principal` 복원

주의:

- 현재 구현은 `roles`를 claim으로 기록합니다.
- 이 값은 principal metadata 운반 용도이며, 리소스 permission policy를 대체하지 않습니다.

### `BCryptPasswordVerifier`

기본 `PasswordVerifier` 구현입니다.

- Spring Security Crypto의 BCrypt를 사용해 해시 비교를 수행합니다.

### `InMemoryRefreshTokenStore`

개발/테스트용 기본 `RefreshTokenStore` 구현입니다.

- `userId::refreshToken`를 key로 저장
- `expiresAt` 기준 만료 검사
- 운영 환경 기본 저장소로는 적합하지 않음

## 의존성

- `support` → `spi`, `common`

## 운영 권장 사항

- 운영에서는 `RefreshTokenStore`를 Redis/DB 기반으로 교체하는 것이 일반적입니다.
- JWT key rotation, issuer/audience, jti 등 고급 정책이 필요하면 `TokenService`를 직접 구현하는 편이 좋습니다.
