# Module: auth-jwt

> 상태: **부분 구현 완료**  
> 현재는 `JwtTokenService` 중심의 기본 구현만 존재한다.  
> JWK/JWKS, key rotation, asymmetric signing은 아직 구현 범위 밖이다.

---

## 목적

`auth-jwt`는 JWT 기반 인증 메커니즘을 담당하는 전략 모듈입니다.

이 모듈은 access/refresh token 발급과 검증, claim 복원, token type 검증 같은 **JWT 전용 로직**만 가져야 합니다.

---

## 포함 범위

- `TokenService`의 JWT 구현체
- JWT claim 직렬화/역직렬화
- access / refresh token 구분
- 토큰 만료 처리
- 토큰 타입 검증
- 향후 확장:
  - issuer / audience validator
  - key resolver
  - JWK / JWKS 지원
  - asymmetric signing

---

## 제외 범위

- Spring Security filter
- SecurityContext 저장
- AutoConfiguration
- OAuth2 성공/실패 핸들러
- cookie 처리
- 세션 저장소

---

## 현재 저장소에서의 매핑

현재 저장소에서 `auth-jwt`로 직접 매핑되는 구현은 다음과 같습니다.

- `support/src/main/java/com/auth/support/jwt/JwtTokenService.java`

현재 `JwtTokenService`의 특징:
- HS256 기반
- `sub = userId`
- `roles` claim 사용
- `token_type` claim 사용 (`access`, `refresh`)

---

## 의존 관계

- 필수 의존:
  - `auth-core`
  - JWT 구현 라이브러리
- 금지 의존:
  - Spring Web / Spring Security
  - Servlet API
  - HttpSession

---

## 공개 API / 대표 타입

### 현재 대표 타입
- `com.auth.support.jwt.JwtTokenService`

### 장기적으로 기대되는 타입
- `JwtTokenService`
- `JwtValidator`
- `JwtClaimsMapper`
- `JwtKeyResolver`
- `JwksKeyResolver`
- `IssuerValidator`
- `AudienceValidator`

### 현재 코드에서 보장되는 규칙
- access token과 refresh token은 `token_type` claim으로 구분한다.
- 검증 시 잘못된 타입이면 `AuthException(INVALID_TOKEN)`을 던진다.
- roles claim이 없으면 빈 리스트로 처리할 수 있다.

---

## 완료 기준

- `auth-jwt`는 `auth-core` 외 다른 내부 모듈 없이 동작해야 한다.
- Spring 관련 클래스가 없어야 한다.
- 현재 `JwtTokenService` 동작을 유지해야 한다.
- 향후 starter에서 Bean 등록만으로 바로 사용할 수 있어야 한다.
