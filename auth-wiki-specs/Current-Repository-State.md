# Current Repository State

## 실제 Gradle 모듈

현재 `settings.gradle`에 포함된 모듈은 다음과 같습니다.

```groovy
include 'contract'
include 'spi'
include 'core'
include 'boot-support'
include 'common'
include 'support'
```

즉 현재 저장소는 **6개 모듈 구조**입니다.

---

## 실제 모듈별 역할

### contract
공개 모델과 예외를 제공합니다.

주요 클래스:
- `AuthException`
- `AuthFailureReason`
- `Principal`
- `User`
- `Tokens`
- `OAuth2UserIdentity`

### spi
코어가 의존하는 확장 포트를 제공합니다.

주요 인터페이스:
- `UserFinder`
- `PasswordVerifier`
- `TokenService`
- `RefreshTokenStore`
- `OAuth2PrincipalResolver`

### common
모듈 공용 유틸을 제공합니다.

주요 클래스:
- `Strings`
- `MoreObjects`

### core
인증 유즈케이스를 제공합니다.

주요 클래스:
- `AuthService`

### support
순수 Java 기반 기본 구현을 제공합니다.

주요 클래스:
- `JwtTokenService`
- `BCryptPasswordVerifier`
- `InMemoryRefreshTokenStore`

### boot-support
Spring Boot 자동설정과 웹/보안 연결을 제공합니다.

주요 클래스:
- `AuthAutoConfiguration`
- `AuthProperties`
- `AuthJwtProperties`
- `RefreshCookieWriter`
- `RefreshTokenExtractor`
- `AuthOncePerRequestFilter`
- `AuthSecurityAutoConfiguration`
- `AuthOAuth2AutoConfiguration`
- `OAuth2AuthenticationSuccessHandler`
- `OAuth2AuthenticationFailureHandler`

---

## 실제 의존 관계

현재 `build.gradle` 기준 의존 방향은 다음과 같습니다.

- `contract` → `common`
- `spi` → `contract`
- `core` → `contract`, `spi`, `common`
- `support` → `spi`, `common`
- `boot-support` → `core`, `common`, `support`

---

## 현재 구조의 장점

- 코어 유즈케이스(`AuthService`)가 프레임워크에 오염되지 않았다.
- `TokenService`, `UserFinder`, `RefreshTokenStore` 같은 포트가 명확하다.
- 기본 JWT/BCrypt/InMemoryRefreshTokenStore 구현이 제공되어 바로 사용할 수 있다.
- Spring Boot에서는 자동설정으로 빠르게 붙일 수 있다.

---

## 현재 구조의 한계

### 1. `boot-support` 책임이 넓다
현재 `boot-support`는 다음 책임을 동시에 수행한다.

- properties 바인딩
- 기본 구현체 Bean 등록
- `AuthService` Bean 조립
- JWT 인증 필터 연결
- 기본 SecurityFilterChain 구성
- OAuth2 성공/실패 핸들러 등록
- refresh cookie 작성/삭제 유틸 제공

즉, **Spring bridge + starter + hybrid wiring**이 한 모듈에 섞여 있다.

### 2. `support`가 전략 기준으로 분리되어 있지 않다
현재 `support`는

- JWT 구현
- BCrypt 비밀번호 검증
- 메모리 기반 refresh token 저장

을 함께 갖고 있다.

이 구조는 작은 프로젝트에는 편하지만,
JWT-only / Session-only / Hybrid 프로젝트별 선택적 사용에는 덜 적합하다.

### 3. Session 전용 모듈이 없다
현재 저장소는 refresh cookie를 다루지만,
전통적인 의미의 **session authentication module**은 없다.

### 4. Hybrid가 독립 개념으로 정리되어 있지 않다
OAuth2 + JWT + cookie 흐름이 `boot-support` 내부 클래스들에 흩어져 있다.

### 5. 공통 테스트 픽스처가 모듈화되어 있지 않다
모듈이 늘어나면 테스트 픽스처 중복이 커질 가능성이 높다.

---

## 리팩터링이 필요한 이유

목표 구조에서는 인증 방식을 다음처럼 선택적으로 가져가야 한다.

- JWT only 프로젝트 → `auth-core + auth-jwt + auth-spring + jwt starter`
- Session only 프로젝트 → `auth-core + auth-session + auth-spring + session starter`
- Hybrid 프로젝트 → `auth-core + auth-jwt + auth-session + auth-hybrid + auth-spring + hybrid starter`

현재 구조는 **실제 동작에는 충분하지만, 확장성 측면에서 모듈 경계가 전략 중심으로 정렬되어 있지 않다.**

---

## 현재 기준으로 문서화할 때 주의할 점

- `auth-session`, `auth-hybrid`, `auth-common-test`는 아직 구현 모듈이 아니다.
- 위키에 이 모듈들을 문서화할 때는 **현재 구현**과 **목표 명세**를 반드시 구분해야 한다.
- `BCryptPasswordVerifier`와 `InMemoryRefreshTokenStore`의 장기 위치는 아직 고정되지 않았다.
  - 현재는 `support`
  - 리팩터링 후에는 임시 유지 또는 별도 support 계층 검토가 필요하다.
