# 설정 레퍼런스

이 문서는 `auth-spring-boot-starter`에서 외부로 드러나는 설정만 설명합니다.
설정 이름은 가능한 한 `auth.*` 아래에 모아두었습니다.

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

## 자주 보는 조합

- JWT만 쓸 때는 `auth.jwt.secret`이 핵심입니다.
- 세션을 쓸 때는 `auth.session.cookie-name`과 `auth.session.ttl`을 확인하면 됩니다.
- OAuth2를 쓸 때는 `spring.security.oauth2.client.*`와 `auth.oauth2.*`를 함께 봐야 합니다.

## 주의사항

- Provider 등록 정보는 `auth.oauth2.*`가 아니라 `spring.security.oauth2.client.*`에서 관리합니다.
- `auth.jwt.secret` 길이가 짧으면 `JwtTokenService` 생성 시 예외가 발생할 수 있습니다.
- `auth.refresh-cookie-secure=true`는 HTTPS 환경에서 사용하는 것이 안전합니다.
- 기본 refresh token 저장소는 `auth-spring-boot-starter`의 메모리 구현입니다.
- 운영에서는 이를 Redis/DB 기반 `RefreshTokenStore`로 교체하는 것을 권장합니다.
- `auth.session.ttl`은 `SessionService`의 기본 TTL에 반영됩니다. session cookie Max-Age는 애플리케이션이 직접 반영합니다.
