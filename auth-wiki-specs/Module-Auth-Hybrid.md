# Module: auth-hybrid

> 상태: **현재 저장소에 독립 구현 없음**  
> 현재는 `boot-support` 내부의 OAuth2/cookie/security wiring 으로 흩어져 있다.

---

## 목적

`auth-hybrid`는 여러 인증 전략을 조합하는 **조합 계층**입니다.

대표적으로 다음 시나리오를 다룹니다.

- 웹은 세션 기반
- 앱/API는 JWT 기반
- OAuth2 로그인 성공 후 내부 JWT 발급
- 세션 우선, JWT fallback

---

## 포함 범위

- 여러 인증 provider 의 조합
- 인증 순서 정책
- 경로/채널 기반 인증 전략 선택
- session + jwt 하이브리드 조합
- OAuth2 이후 내부 인증 컨텍스트 연결 정책

권장 타입:
- `CompositeAuthenticationProvider`
- `HybridAuthenticationProvider`
- `AuthenticationStrategyResolver`
- `PathBasedAuthenticationStrategy`

---

## 제외 범위

- OAuth2 Provider 자체 설정
- Spring Security filter chain 자동 구성
- JWT 구현 세부 사항
- session 저장소 구현 세부 사항

---

## 현재 저장소에서의 매핑

현재 저장소에는 `auth-hybrid` 독립 모듈이 없다.

다만 다음 클래스들이 하이브리드 책임의 일부를 이미 갖고 있다.

- `AuthOAuth2AutoConfiguration`
- `OAuth2AuthenticationSuccessHandler`
- `OAuth2AuthenticationFailureHandler`
- `RefreshCookieWriter`
- `RefreshTokenExtractor`

즉 현재는 하이브리드 로직이 `boot-support` 안에 분산되어 있다.

---

## 의존 관계

- 필수 의존:
  - `auth-core`
- 일반적 선택 의존:
  - `auth-jwt`
  - `auth-session`
- 금지 의존:
  - Spring Boot 자동설정을 강제하는 직접 구성

---

## 공개 API / 대표 타입

### 제안 공개 타입
- `HybridAuthenticationProvider`
- `CompositeAuthenticationProvider`
- `AuthenticationStrategyResolver`
- `ChannelAuthenticationPolicy`
- `PathAuthenticationPolicy`

### 설계 원칙
- JWT와 Session 내부 구현을 재구현하지 않는다.
- 하이브리드 모듈은 “조합”만 담당한다.
- Spring Web 연동은 `auth-spring` 또는 starter 에서 수행한다.

---

## 완료 기준

- `auth-hybrid`는 JWT/Session 전략을 조합하는 역할만 해야 한다.
- `auth-jwt`와 `auth-session`을 직접 대체하지 않아야 한다.
- starter 없이도 조합 정책 자체는 테스트 가능해야 한다.
