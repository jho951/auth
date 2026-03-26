# Module: auth-spring

> 상태: **현재 구현이 `boot-support`에 흩어져 있음**  
> 실제 리팩터링에서는 Spring bridge와 starter 조립을 먼저 분리해야 한다.

---

## 목적

`auth-spring`은 core 결과를 Spring Web / Spring Security 와 연결하는 **bridge module** 입니다.

이 모듈의 핵심은 “인증 메커니즘 구현”이 아니라  
“인증 결과를 Spring 런타임에서 소비하게 연결”하는 것입니다.

---

## 포함 범위

- Spring Security 인증 객체 변환
- `SecurityContext` 연결
- 공통 filter base
- 예외를 HTTP 응답으로 변환하는 entry point / handler
- `@CurrentUser` 같은 사용자 주입 편의 기능
- request/response 계층 어댑터

---

## 제외 범위

- JWT 서명/검증
- 세션 저장소 구현
- AutoConfiguration
- starter 조립
- OAuth2 provider wiring 전체

---

## 현재 저장소에서의 매핑

현재 저장소에서 `auth-spring`에 가장 가까운 구현은 다음과 같다.

- `AuthOncePerRequestFilter`
- `AuthSecurityAutoConfiguration`의 일부 보안 연결 로직
- `RefreshTokenExtractor`의 웹 의존 부분

다만 현재는 이 책임들이 `boot-support`에 섞여 있다.

---

## 의존 관계

- 필수 의존:
  - `auth-core`
  - Spring Web
  - Spring Security
- 금지 의존:
  - JWT 라이브러리 직접 강제
  - starter 전용 properties binding

---

## 공개 API / 대표 타입

### 현재 대표 타입
- `AuthOncePerRequestFilter`

### 제안 타입
- `AuthPrincipalAuthenticationToken`
- `CurrentUser`
- `CurrentUserArgumentResolver`
- `AuthAuthenticationEntryPoint`
- `AuthAccessDeniedHandler`
- `AbstractAuthFilter`

### 중요한 원칙
- `auth-spring`은 재사용 가능한 bridge 여야 한다.
- 어떤 인증 모드를 켤지는 starter 가 결정한다.
- mode 분기와 properties 해석은 `auth-spring`이 아니라 starter 에 둔다.

---

## 완료 기준

- `auth-spring`은 Spring bridge 클래스만 포함해야 한다.
- AutoConfiguration 클래스는 starter 로 이동해야 한다.
- JWT only / Session only / Hybrid starter 모두가 공통으로 재사용 가능해야 한다.
