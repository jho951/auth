# 설정 레퍼런스

이 문서는 **현재 구현된 `auth-spring` 및 `auth-spring-boot-starter` 기준** 설정을 설명합니다.

## 프로퍼티 바인딩 클래스

- `auth-spring/src/main/java/com/auth/config/AuthProperties.java`
- `auth-spring-boot-starter/src/main/java/com/auth/config/jwt/AuthJwtProperties.java`
- `auth-spring-boot-starter/src/main/java/com/auth/session/config/AuthSessionProperties.java`

## `auth.*`

| 키 | 기본값 | 설명 |
|---|---|---|
| `auth.bearer-prefix` | `Bearer ` | `Authorization` 헤더 접두사 |
| `auth.refresh-cookie-enabled` | `true` | refresh cookie 사용 여부 |
| `auth.refresh-cookie-name` | `refresh_token` | refresh cookie 이름 |
| `auth.refresh-cookie-http-only` | `true` | HttpOnly 속성 |
| `auth.refresh-cookie-secure` | `true` | Secure 속성 |
| `auth.refresh-cookie-path` | `/` | cookie path |
| `auth.refresh-cookie-same-site` | `Lax` | SameSite 속성 |
| `auth.auto-security` | `true` | 기본 `SecurityFilterChain` 자동 구성 여부 |
| `auth.oauth2.enabled` | `true` | OAuth2 success/failure handler 자동 구성 여부 |
| `auth.oauth2.authorization-base-uri` | `/oauth2/authorization` | OAuth2 로그인 시작 URI |
| `auth.oauth2.login-processing-base-uri` | `/login/oauth2/code/*` | OAuth2 callback 처리 URI |

## `auth.jwt.*`

| 키 | 기본값 | 설명 |
|---|---|---|
| `auth.jwt.secret` | 없음 | JWT 서명 비밀키. HS256 기준 최소 32바이트 권장 |
| `auth.jwt.access-seconds` | `900` | access token 만료 시간(초) |
| `auth.jwt.refresh-seconds` | `1209600` | refresh token 만료 시간(초, 14일) |

## `auth.session.*`

| 키 | 기본값 | 설명 |
|---|---|---|
| `auth.session.cookie-name` | `AUTH_SESSION` | session cookie 이름 |
| `auth.session.ttl` | `PT1H` | session TTL |

## 최소 예시

```yaml
auth:
  refresh-cookie-name: ADMIN_REFRESH_TOKEN
  refresh-cookie-secure: true
  oauth2:
    enabled: true
  jwt:
    secret: ${AUTH_JWT_SECRET}
    access-seconds: 3600
    refresh-seconds: 1209600
  session:
    cookie-name: AUTH_SESSION
    ttl: PT1H
```

## 자동 구성 조건

### `TokenService`

- `TokenService` 빈이 없고
- `auth.jwt.secret` 값이 있을 때
- `JwtTokenService`가 기본 구현으로 등록됩니다.

### `PasswordVerifier`

- `PasswordVerifier` 빈이 없을 때
- `BCryptPasswordVerifier`가 기본 구현으로 등록됩니다.

### `RefreshTokenStore`

- `RefreshTokenStore` 빈이 없을 때
- `InMemoryRefreshTokenStore`가 기본 구현으로 등록됩니다.

### `SessionStore` / `SessionAuthenticationProvider`

- `SessionStore` 빈이 없을 때
- `SimpleSessionStore`가 기본 구현으로 등록됩니다.

### OAuth2 handler

- `spring-security-oauth2-client`가 classpath에 있고
- `OAuth2PrincipalResolver` 빈이 있고
- `auth.oauth2.enabled=true`일 때
- OAuth2 success/failure handler가 자동 등록됩니다.

## 주의사항

- Provider 등록 정보는 `auth.oauth2.*`가 아니라 `spring.security.oauth2.client.*`에서 관리합니다.
- `auth.jwt.secret` 길이가 짧으면 `JwtTokenService` 생성 시 예외가 발생할 수 있습니다.
- `auth.refresh-cookie-secure=true`는 HTTPS 환경에서 사용하는 것이 안전합니다.
- 운영에서는 `InMemoryRefreshTokenStore` 대신 Redis/DB 기반 구현을 권장합니다.
- `auth.session.ttl`은 `SessionService`의 기본 TTL과 `SessionCookie` Max-Age 설정에 반영됩니다.
