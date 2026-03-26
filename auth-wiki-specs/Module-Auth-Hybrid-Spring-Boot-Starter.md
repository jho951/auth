# Module: auth-hybrid-spring-boot-starter

> 상태: **현재 `boot-support`가 사실상 이 역할을 겸하고 있음**

---

## 목적

`auth-hybrid-spring-boot-starter`는 세션, JWT, OAuth2 연결을 함께 다루는 **하이브리드 조립 계층**입니다.

현재 저장소에서 가장 가까운 실제 구현은 `boot-support` 입니다.

---

## 포함 범위

- OAuth2 로그인 성공/실패 핸들러 등록
- refresh cookie writer / extractor 조립
- 하이브리드 인증 순서 설정
- 필요 시 path/channel 기반 security wiring
- session + jwt + oauth2 흐름 연결

---

## 제외 범위

- JWT 서명/검증 구현 자체
- 세션 저장소 구현 자체
- UserFinder 구현
- OAuth2 Provider 클라이언트 등록

---

## 현재 저장소에서의 매핑

현재 저장소에서 이 starter 로 이동할 핵심 클래스는 다음이다.

- `AuthOAuth2AutoConfiguration`
- `OAuth2AuthenticationSuccessHandler`
- `OAuth2AuthenticationFailureHandler`
- `RefreshCookieWriter`
- `RefreshTokenExtractor`

필요 시 `AuthSecurityAutoConfiguration`의 일부 hybrid wiring 도 함께 이동한다.

---

## 의존 관계

- 필수 의존:
  - `auth-core`
  - `auth-jwt`
  - `auth-session`
  - `auth-hybrid`
  - `auth-spring`
  - Spring Boot AutoConfigure
- 선택 의존:
  - Spring Security OAuth2 Client

---

## 공개 API / 대표 타입

### 현재 대표 타입
- `AuthOAuth2AutoConfiguration`
- `OAuth2AuthenticationSuccessHandler`
- `OAuth2AuthenticationFailureHandler`
- `RefreshCookieWriter`
- `RefreshTokenExtractor`

### 설계 원칙
- OAuth2 provider 등록(`spring.security.oauth2.client.*`)은 애플리케이션이 담당한다.
- starter 는 로그인 성공 이후 내부 Principal 매핑과 JWT 발급 연결만 담당한다.
- refresh cookie 정책은 properties 기반으로 주입한다.

---

## 완료 기준

- OAuth2 + JWT 하이브리드 프로젝트에서 기본 구성이 가능해야 한다.
- JWT only 프로젝트에는 이 starter 가 필요하지 않아야 한다.
- `auth-hybrid`와 `auth-spring` 조합만 담당해야 한다.
