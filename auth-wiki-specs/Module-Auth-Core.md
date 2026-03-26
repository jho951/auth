# Module: auth-core

> 상태: **현재 저장소에 가장 많이 구현되어 있는 핵심 모듈**  
> 현재 구현 자산의 대부분은 `auth-core`로 자연스럽게 이동 가능하다.

---

## 목적

`auth-core`는 이 저장소의 **가장 아래층**입니다.

이 모듈은 인증 결과를 표현하는 모델, 실패 모델, 확장 포트, 공통 유즈케이스를 담습니다.  
Spring Boot, JWT 라이브러리, 세션 구현체 유무와 상관없이 항상 재사용 가능한 구조를 유지해야 합니다.

---

## 포함 범위

- 공개 모델
  - `Principal`
  - `User`
  - `Tokens`
  - `OAuth2UserIdentity`
- 예외/실패 분류
  - `AuthException`
  - `AuthFailureReason`
- SPI
  - `UserFinder`
  - `PasswordVerifier`
  - `TokenService`
  - `RefreshTokenStore`
  - `OAuth2PrincipalResolver`
- 공통 유즈케이스
  - `AuthService`
- 공용 유틸
  - `Strings`
  - `MoreObjects`

---

## 제외 범위

- JWT 서명/파싱 라이브러리 연동
- Spring Security filter/entrypoint/argument resolver
- HttpSession 직접 접근
- AutoConfiguration
- OAuth2 Provider 등록과 화면 흐름

---

## 현재 저장소에서의 매핑

현재 저장소에서는 다음 모듈을 `auth-core`로 통합하는 것이 목표입니다.

- `contract`
- `spi`
- `common`
- `core`

즉 현재의 `com.auth.api.*`, `com.auth.spi.*`, `com.auth.common.*`, `com.auth.core.service.AuthService`는
리팩터링 후 `auth-core`의 공개 API가 됩니다.

---

## 의존 관계

- 허용 의존:
  - Java 표준 라이브러리
- 금지 의존:
  - Spring Boot / Spring Security
  - JJWT 등 JWT 구현 라이브러리
  - Servlet API
  - HttpSession

---

## 공개 API / 대표 타입

### 현재 기준 공개 타입
- `com.auth.api.model.Principal`
- `com.auth.api.model.User`
- `com.auth.api.model.Tokens`
- `com.auth.api.model.OAuth2UserIdentity`
- `com.auth.api.exception.AuthException`
- `com.auth.api.exception.AuthFailureReason`
- `com.auth.spi.UserFinder`
- `com.auth.spi.PasswordVerifier`
- `com.auth.spi.TokenService`
- `com.auth.spi.RefreshTokenStore`
- `com.auth.spi.OAuth2PrincipalResolver`
- `com.auth.core.service.AuthService`

### 패키지 방향
초기 마이그레이션 단계에서는 package rename을 최소화할 수 있다.  
즉, Gradle 모듈만 `auth-core`로 먼저 통합하고 package 는 추후 정리해도 된다.

---

## 완료 기준

- `auth-core` 단독으로 컴파일 가능해야 한다.
- Spring 관련 의존성이 없어야 한다.
- `AuthService`가 현재와 동일한 로그인/재발급/로그아웃 유즈케이스를 유지해야 한다.
- 현재 `contract`, `spi`, `common`, `core`의 공개 API가 기능적으로 보존되어야 한다.
