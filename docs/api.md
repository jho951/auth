# API 가이드

현재 코드 기준으로 이 저장소는 기본 로그인/재발급/로그아웃 HTTP 엔드포인트를 제공하지 않습니다.
`contract`, `core`, `spi`, `support` 모듈은 순수 Java 또는 지원 구현 모듈이며, `boot-support` 모듈은 Spring Boot 통합을 위한 조립 계층만 제공합니다.

## HTTP API 제공 방식

인증 관련 HTTP API는 이 모듈이 아니라, 이 모듈을 사용하는 서비스 애플리케이션이 직접 정의해야 합니다.

일반적으로 애플리케이션은 아래 구성요소를 조합해 로그인/재발급/로그아웃 API를 만듭니다.

- `AuthService`
- `RefreshCookieWriter`
- `RefreshTokenExtractor`
- 애플리케이션 전용 요청/응답 DTO
- 애플리케이션 전용 응답 래퍼 (`GlobalResponse` 등)

## OAuth2 Integration Endpoints

OAuth2 연동 경로는 `auth.oauth2.enabled=true` 일 때 활성화됩니다.
기본값은 활성화이며, 서비스 애플리케이션에 `spring.security.oauth2.client.registration.*` 설정과 `OAuth2PrincipalResolver` 빈이 있어야 정상 동작합니다.

| Method | Path | 설명 | 인증 |
|---|---|---|---|
| `GET` | `/oauth2/authorization/{registrationId}` | OAuth2 로그인 시작 | 필요 없음 |
| `GET` | `/login/oauth2/code/{registrationId}` | OAuth2 Provider callback 처리 | 필요 없음 |

## 애플리케이션 API 예시

예를 들어 서비스 애플리케이션은 다음과 같은 경로를 직접 제공할 수 있습니다.

| Method | Path | 설명 | 구현 주체 |
|---|---|---|---|
| `POST` | `/login` | 로그인 | 서비스 애플리케이션 |
| `POST` | `/refresh` | Access Token 재발급 | 서비스 애플리케이션 |
| `POST` | `/logout` | 로그아웃 | 서비스 애플리케이션 |

이 경로와 응답 구조는 모듈이 강제하지 않습니다.

### OAuth2 로그인 성공 응답

OAuth2 로그인 성공 시 응답 본문:

```json
{
  "accessToken": "..."
}
```

추가 동작:
- `auth.refresh-cookie-enabled=true`이면 Refresh Token 쿠키를 함께 내려줍니다.

## Authentication Notes

- Access Token은 기본적으로 다음 헤더 형식으로 사용합니다.

```http
Authorization: Bearer <access-token>
```

- 접두사는 `auth.bearer-prefix`로 변경할 수 있습니다.
- Refresh 쿠키 이름 기본값은 `refresh_token`이며 `auth.refresh-cookie-name`으로 변경할 수 있습니다.

## Error Handling

- 비즈니스 오류는 `AuthException`과 `ErrorCode`로 표현됩니다.
- 대표 에러 코드는 다음과 같습니다.

| ErrorCode | 설명 |
|---|---|
| `INVALID_REQUEST` | 필수 인자 또는 쿠키 누락 |
| `USER_NOT_FOUND` | 사용자 조회 실패 |
| `INVALID_CREDENTIALS` | 비밀번호 불일치 |
| `INVALID_TOKEN` | 토큰 서명, 형식, 타입 오류 |
| `TOKEN_REVOKED` | 이미 폐기된 Refresh Token |

## Module Boundary Notes

- 현재 코드 기준으로 이 저장소에 명시적 REST 컨트롤러는 없습니다.
- OAuth2 callback 성공 응답은 컨트롤러가 아니라 Spring Security success handler에서 처리됩니다.
- `contract` 모듈은 모델과 예외만 제공합니다.
- `core` 모듈은 `AuthService` 중심의 인증 유즈케이스를 제공합니다.
- `spi` 모듈은 `UserFinder`, `PasswordVerifier`, `TokenService`, `RefreshTokenStore`, `OAuth2PrincipalResolver` 확장 포인트를 제공합니다.
- `support` 모듈은 기본 구현체를 제공합니다.
- `boot-support` 모듈은 `RefreshCookieWriter`, `RefreshTokenExtractor`, `AuthOncePerRequestFilter`, 자동 설정을 제공합니다.
- `common` 모듈은 공용 유틸만 제공합니다.
- 현재 코드 기준으로 별도 `Health Check`, `Admin API`, `User Management API`는 이 저장소에 없습니다.
