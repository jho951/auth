# Module: spi

## 역할

`spi`는 애플리케이션 또는 외부 구현체가 채우는 **확장 포트**를 제공합니다.

## 포함 인터페이스

- `UserFinder`
- `PasswordVerifier`
- `TokenService`
- `RefreshTokenStore`
- `OAuth2PrincipalResolver`

## 핵심 계약

### `UserFinder`

- 입력: username
- 출력: `Optional<User>`
- 사용처: `AuthService.login(username, password)`

애플리케이션은 각자 사용하는 저장소 방식에 맞춰 이 인터페이스를 구현합니다.

### `PasswordVerifier`

- 입력: 평문 비밀번호, 저장된 해시
- 출력: 일치 여부

기본 구현은 `support`의 `BCryptPasswordVerifier`입니다.

### `TokenService`

- access/refresh token 발급
- access/refresh token 검증 후 `Principal` 복원

기본 구현은 `support`의 `JwtTokenService`입니다.

### `RefreshTokenStore`

- `save(userId, refreshToken, expiresAt)`
- `exists(userId, refreshToken)`
- `revoke(userId, refreshToken)`

기본 구현은 `support`의 `InMemoryRefreshTokenStore`입니다.
운영 환경에서는 Redis나 DB 기반 구현으로 교체하는 것이 일반적입니다.

### `OAuth2PrincipalResolver`

- 입력: `OAuth2UserIdentity`
- 출력: 내부 `Principal`

OAuth2 Provider 인증이 끝난 뒤, 내부 사용자 연결과 역할 결정은 애플리케이션이 담당합니다.

## 의존성

- `spi` → `contract`

## 설계 원칙

- `spi`는 Spring, DB, Redis, OAuth2 Provider SDK 구현에 직접 묶이지 않습니다.
- `core`는 구현체를 모르고 `spi` 인터페이스에만 의존합니다.
