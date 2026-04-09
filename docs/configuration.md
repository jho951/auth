# 설정 레퍼런스

이 문서는 `auth-spring`에서 외부로 드러나는 설정만 설명합니다.
설정 이름은 가능한 한 `auth.*` 아래에 모아두었습니다.

## 프로퍼티 바인딩 클래스

- `auth-spring/src/main/java/com/auth/config/AuthProperties.java`

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
| `auth.auto-security` | `true` | Security 조립을 켤지 나타내는 플래그 |
| `auth.oauth2.enabled` | `true` | OAuth2 연동을 켤지 나타내는 플래그 |
| `auth.oauth2.authorization-base-uri` | `/oauth2/authorization` | OAuth2 로그인 시작 URI |
| `auth.oauth2.login-processing-base-uri` | `/login/oauth2/code/*` | OAuth2 callback 처리 URI |

## 최소 예시

```yaml
auth:
  refresh-cookie-name: ADMIN_REFRESH_TOKEN
  refresh-cookie-secure: true
  oauth2:
    enabled: true
```

## 자주 보는 조합

- `auth.*` 값은 `AuthProperties`에서 바인딩됩니다.
- OAuth2를 쓸 때는 `spring.security.oauth2.client.*`와 `auth.oauth2.*`를 함께 봐야 합니다.

## 주의사항

- Provider 등록 정보는 `auth.oauth2.*`가 아니라 `spring.security.oauth2.client.*`에서 관리합니다.
- `auth.refresh-cookie-secure=true`는 HTTPS 환경에서 사용하는 것이 안전합니다.
