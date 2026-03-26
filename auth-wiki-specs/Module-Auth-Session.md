# Module: auth-session

> 상태: **현재 저장소에 독립 구현 없음**  
> 이 문서는 목표 구조의 책임을 명시하는 설계 문서다.

---

## 목적

`auth-session`은 세션 기반 인증을 위한 **전략 모듈**입니다.

현재 저장소에는 전통적인 의미의 session authentication module 이 없기 때문에,  
이 모듈은 위키 기준으로 먼저 명세를 정의하고 이후 구현하는 **spec-first module** 입니다.

---

## 포함 범위

- 세션 인증 추상화
- 세션 식별자 기반 사용자 복원
- 세션 저장소 추상화
- 세션 principal 매핑
- 세션 인증 provider

권장 타입:
- `SessionStore`
- `SessionEntry`
- `SessionCredential`
- `SessionAuthenticationProvider`
- `SessionPrincipalMapper`

---

## 제외 범위

- Spring `HttpSession` 직접 의존
- SecurityFilterChain 자동설정
- OAuth2 로그인 처리
- JWT 발급/검증
- 쿠키 작성/삭제의 웹 어댑터 세부 구현

---

## 현재 저장소에서의 매핑

현재 저장소에 `auth-session`에 직접 대응되는 구현은 없다.

다만 다음 흐름이 향후 session 모듈 설계와 연관된다.
- `RefreshTokenExtractor`가 cookie 에서 값을 읽는다.
- `RefreshCookieWriter`가 cookie 를 쓴다.

하지만 이는 **refresh token cookie 처리**이지, 일반적인 세션 인증 구현은 아니다.

---

## 의존 관계

- 필수 의존:
  - `auth-core`
- 선택 의존:
  - 없음
- 금지 의존:
  - Spring Boot 자동설정
  - JWT 구현체

---

## 공개 API / 대표 타입

### 제안 공개 타입
- `SessionStore`
- `SessionEntry`
- `SessionPrincipalMapper`
- `SessionCredential`
- `SessionAuthenticationProvider`

### 설계 원칙
- 가능하면 `HttpSession` 그 자체보다 상위 추상화에 의존한다.
- 웹/서블릿 어댑터는 `auth-spring` 또는 session starter 에 둔다.
- 서비스 코드는 “세션 기반인지”를 모르고 최종 principal 만 받도록 한다.

---

## 완료 기준

- `auth-session`은 `auth-core`만 의존해야 한다.
- Spring 없이도 최소한 세션 저장소 추상화를 정의할 수 있어야 한다.
- session 인증의 핵심 인터페이스가 고정되어야 한다.
- session only 프로젝트가 JWT 구현체 없이 사용할 수 있어야 한다.
