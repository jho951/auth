# API 가이드

## 먼저 알아둘 점

이 저장소는 **로그인/재발급/로그아웃 REST 컨트롤러를 제공하지 않습니다.**

애플리케이션이 자신의 URI와 응답 형식에 맞춰 직접 엔드포인트를 구성해야 합니다.

즉, 이 저장소가 제공하는 것은 다음입니다.

- `AuthService`
- `RefreshCookieWriter`
- `RefreshTokenExtractor`
- `AuthOncePerRequestFilter`
- `SessionAuthenticationFilter`
- OAuth2 success/failure handler

## 애플리케이션이 직접 만드는 엔드포인트 예시

| Method | Path | 설명 |
|---|---|---|
| `POST` | `/login` | username/password 로그인 |
| `POST` | `/refresh` | refresh rotation |
| `POST` | `/logout` | refresh token 폐기 |

## OAuth2 관련 엔드포인트

`auth-spring-boot-starter`와 Spring Security OAuth2 Client가 함께 구성되면, 기본 OAuth2 엔드포인트는 Spring Security가 처리합니다.

| Method | Path | 설명 |
|---|---|---|
| `GET` | `/oauth2/authorization/{registrationId}` | OAuth2 로그인 시작 |
| `GET` | `/login/oauth2/code/{registrationId}` | OAuth2 callback 처리 |

경로는 `auth.oauth2.authorization-base-uri`, `auth.oauth2.login-processing-base-uri`로 조정할 수 있습니다.

## OAuth2 성공 응답

기본 success handler는 아래 형식으로 응답합니다.

```http
200 OK
Set-Cookie: refresh_token=...; HttpOnly; Secure; Path=/; SameSite=Lax
Content-Type: application/json

{"accessToken":"..."}
```

실패 시 기본 failure handler는 다음 JSON을 반환합니다.

```http
401 Unauthorized
Content-Type: application/json

{"message":"OAUTH2_AUTHENTICATION_FAILED"}
```

## 일반 로그인/재발급/로그아웃 예시 구성

### 로그인

- 요청 DTO를 애플리케이션이 정의
- `AuthService.login(username, password)` 호출
- 필요하면 `RefreshCookieWriter.write(tokens, ResponseEntity.ok(...))` 사용

### 재발급

- 쿠키 사용 시 `RefreshTokenExtractor.extract(request)`로 refresh token 추출
- `AuthService.refresh(refreshToken)` 호출
- 필요하면 새 refresh cookie 다시 기록

### 로그아웃

- 쿠키 사용 시 `RefreshTokenExtractor.extract(request)`로 refresh token 추출
- `AuthService.logout(refreshToken)` 호출
- 필요하면 `RefreshCookieWriter.clear(...)` 사용

## 세션 기반 API 예시

`auth-session`을 사용할 때는 보통 다음 엔드포인트를 애플리케이션이 직접 제공합니다.

- `POST /session/login`
- `POST /session/logout`
- `GET /session/me`

필요한 조립 요소는 `SessionService`, `SessionCookieExtractor`, `SessionAuthenticationFilter`입니다.

## 내부 에러 분류

`AuthException`은 다음 `AuthFailureReason` 중 하나를 가질 수 있습니다.

| Reason | 의미 |
|---|---|
| `INVALID_INPUT` | 입력값 누락/형식 오류 |
| `USER_NOT_FOUND` | 사용자 조회 실패 |
| `INVALID_CREDENTIALS` | 비밀번호 불일치 |
| `INVALID_TOKEN` | 토큰 서명/형식/만료/타입 오류 |
| `REVOKED_TOKEN` | 서버 저장소 기준으로 폐기된 refresh token |
| `INTERNAL` | 내부 오류 |

이 값은 HTTP status mapping 자체를 강제하지 않습니다.

어떤 status/응답 포맷으로 외부에 노출할지는 애플리케이션이 결정합니다.
