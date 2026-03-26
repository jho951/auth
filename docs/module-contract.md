# Module: contract

## 역할

`contract`는 다른 모듈과 애플리케이션이 공유하는 **공개 모델과 예외 계약**을 제공합니다.

## 포함 클래스

- `com.auth.api.model.Principal`
- `com.auth.api.model.User`
- `com.auth.api.model.Tokens`
- `com.auth.api.model.OAuth2UserIdentity`
- `com.auth.api.exception.AuthException`
- `com.auth.api.exception.AuthFailureReason`

## 핵심 포인트

### `Principal`

현재 인증 결과를 나타내는 최소 모델입니다.

- `userId`
- `roles`
- `hasRole(String)` convenience API

주의:

- 현재 `roles`와 `hasRole()`이 존재하지만, 이 저장소는 리소스 단위 permission engine을 제공하지 않습니다.
- `Principal`은 현재 사용자 식별과 보안 메타데이터 전달용으로 사용합니다.

### `User`

`AuthService.login(username, password)`가 다루는 **인증용 최소 사용자 모델**입니다.

- `userId`
- `username`
- `passwordHash`
- `roles`

도메인 엔티티 전체를 노출하지 않고, 인증에 필요한 값만 담습니다.

### `Tokens`

토큰 발급 결과를 나타냅니다.

- `accessToken`
- `refreshToken`

쿠키 기록은 이 모듈이 아니라 `boot-support`의 `RefreshCookieWriter`가 담당합니다.

### `OAuth2UserIdentity`

외부 OAuth2/OIDC Provider에서 온 사용자 정보를 내부 principal로 바꾸기 위한 중립 모델입니다.

## 의존성

- `contract` → `common`

## 아웃바운드 계약 성격

`contract`는 가능하면 다른 구현 모듈에 의존하지 않도록 유지하는 것이 좋습니다. 현재는 입력 검증 유틸 때문에 `common`에 의존합니다.
