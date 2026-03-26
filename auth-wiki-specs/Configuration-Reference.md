# Configuration Reference

## 문서 범위

이 문서는 **현재 저장소에 실제 존재하는 설정**과  
**목표 starter 구조에서 추가될 설정**을 함께 정리합니다.

표기 규칙:

- **구현됨**: 현재 코드에 이미 존재
- **예정**: 목표 구조에서 도입 예정

---

## 현재 구현된 설정

### `auth.*`

| 키 | 기본값 | 상태 | 설명 |
| --- | --- | --- | --- |
| `auth.bearer-prefix` | `Bearer ` | 구현됨 | Authorization 헤더 접두사 |
| `auth.refresh-cookie-enabled` | `true` | 구현됨 | refresh cookie 사용 여부 |
| `auth.refresh-cookie-name` | `refresh_token` | 구현됨 | refresh cookie 이름 |
| `auth.refresh-cookie-http-only` | `true` | 구현됨 | HttpOnly 여부 |
| `auth.refresh-cookie-secure` | `true` | 구현됨 | Secure 여부 |
| `auth.refresh-cookie-path` | `/` | 구현됨 | cookie path |
| `auth.refresh-cookie-same-site` | `Lax` | 구현됨 | SameSite 값 |
| `auth.auto-security` | `true` | 구현됨 | 기본 SecurityFilterChain 자동 구성 여부 |

### `auth.jwt.*`

| 키 | 기본값 | 상태 | 설명 |
| --- | --- | --- | --- |
| `auth.jwt.secret` | 없음 | 구현됨 | HS256 서명 키 |
| `auth.jwt.access-seconds` | `900` | 구현됨 | access token TTL |
| `auth.jwt.refresh-seconds` | `1209600` | 구현됨 | refresh token TTL |

### `auth.oauth2.*`

| 키 | 기본값 | 상태 | 설명 |
| --- | --- | --- | --- |
| `auth.oauth2.enabled` | `true` | 구현됨 | OAuth2 support 자동 구성 여부 |
| `auth.oauth2.authorization-base-uri` | `/oauth2/authorization` | 구현됨 | OAuth2 시작 URI |
| `auth.oauth2.login-processing-base-uri` | `/login/oauth2/code/*` | 구현됨 | callback URI |

---

## 목표 구조에서 예상되는 설정

### `auth.session.*`

| 키 | 상태 | 설명 |
| --- | --- | --- |
| `auth.session.cookie-name` | 예정 | session cookie 이름 |
| `auth.session.lookup-mode` | 예정 | session 복원 전략 |
| `auth.session.fail-on-missing` | 예정 | 세션 미존재 시 처리 정책 |

### `auth.hybrid.*`

| 키 | 상태 | 설명 |
| --- | --- | --- |
| `auth.hybrid.order` | 예정 | 예: `session,jwt` |
| `auth.hybrid.public-paths` | 예정 | 하이브리드 보안 체인에서 예외 처리할 경로 |
| `auth.hybrid.oauth2-success-mode` | 예정 | OAuth2 성공 후 처리 모드 |

---

## 예시: 현재 JWT 기반 설정

```yaml
auth:
  bearer-prefix: "Bearer "
  refresh-cookie-enabled: true
  refresh-cookie-name: "ADMIN_REFRESH_TOKEN"
  refresh-cookie-http-only: true
  refresh-cookie-secure: true
  refresh-cookie-path: "/"
  refresh-cookie-same-site: "Lax"
  auto-security: true

  oauth2:
    enabled: true
    authorization-base-uri: "/oauth2/authorization"
    login-processing-base-uri: "/login/oauth2/code/*"

  jwt:
    secret: ${AUTH_JWT_SECRET}
    access-seconds: 3600
    refresh-seconds: 1209600
```

---

## 실제 코드와 연결되는 위치

- `AuthProperties`
- `AuthJwtProperties`
- `AuthAutoConfiguration`
- `AuthSecurityAutoConfiguration`
- `AuthOAuth2AutoConfiguration`
- `RefreshCookieWriter`

---

## 주의사항

- 현재 `auth.jwt.secret`는 HS256 최소 32바이트를 만족해야 한다.
- `auth.jwt.refresh-seconds`는 refresh token 만료, 저장소 TTL, cookie max-age에 함께 사용된다.
- OAuth2 provider 등록 자체는 `auth.oauth2.*`가 아니라 `spring.security.oauth2.client.*`로 설정한다.
- `auth.refresh-cookie-secure=true`는 HTTPS 환경을 기본으로 전제한다.

---

## starter 별 설정 원칙

### JWT starter
- `auth.jwt.*`
- `auth.bearer-prefix`
- `auth.auto-security`

### Session starter
- `auth.session.*`
- 필요 시 공통 `auth.*`

### Hybrid starter
- `auth.jwt.*`
- `auth.oauth2.*`
- `auth.hybrid.*`
- refresh cookie 관련 `auth.*`
