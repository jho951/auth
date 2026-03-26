# Module: core

## 역할

`core`는 인증 유즈케이스를 수행하는 **도메인 서비스**를 제공합니다.

## 핵심 클래스

- `com.auth.core.service.AuthService`

## `AuthService`가 하는 일

### `login(username, password)`

- `UserFinder`로 사용자 조회
- `PasswordVerifier`로 비밀번호 검증
- `Principal` 생성
- `TokenService`로 access/refresh 발급
- `RefreshTokenStore`에 refresh 저장

### `login(principal)`

이미 외부에서 인증이 끝난 사용자를 기준으로 access/refresh를 발급합니다.
대표적인 사용처는 OAuth2 로그인 성공 후 내부 principal이 이미 준비된 경우입니다.

### `refresh(refreshToken)`

- refresh token 검증
- 저장소 기준 유효성 확인
- 기존 refresh token 폐기
- 새 access/refresh 발급
- 새 refresh token 저장

### `logout(refreshToken)`

- refresh token 검증
- 저장소에서 해당 refresh token 폐기

## 의존성

- `core` → `contract`, `spi`, `common`

## 설계 원칙

- Spring MVC, HTTP, Servlet에 직접 의존하지 않습니다.
- DB나 Redis 구현을 직접 모르고 `spi` 인터페이스만 사용합니다.
- 응답 포맷과 HTTP status mapping은 애플리케이션이 담당합니다.
