# Module: auth-jwt-spring-boot-starter

> 상태: **현재 `boot-support`에서 분리 예정**

---

## 목적

`auth-jwt-spring-boot-starter`는 JWT 기반 프로젝트를 위한 **조립 계층**입니다.

이 모듈은 구현을 담지 않고,
`auth-core + auth-jwt + auth-spring`을 Spring Boot 애플리케이션에서 바로 사용할 수 있게 묶습니다.

---

## 포함 범위

- `@ConfigurationProperties`
- JWT 관련 AutoConfiguration
- JWT bean 조립
- 필터 등록
- 기본 SecurityFilterChain 연결
- refresh cookie 관련 공통 Bean 조립(필요 시)

---

## 제외 범위

- JWT 서명 알고리즘 구현
- Principal 모델 정의
- 세션 인증 구현
- OAuth2 성공/실패 핸들러

---

## 현재 저장소에서의 매핑

현재 저장소에서 이 모듈로 분리될 대상은 주로 다음입니다.

- `AuthAutoConfiguration` 중 JWT 관련 bean 조립
- `AuthProperties`
- `AuthJwtProperties`
- `AuthSecurityAutoConfiguration` 중 JWT 필터/보안 체인 관련 부분

---

## 의존 관계

- 필수 의존:
  - `auth-core`
  - `auth-jwt`
  - `auth-spring`
  - Spring Boot AutoConfigure
- 금지 의존:
  - session 전략 구현
  - hybrid 전략 구현

---

## 공개 API / 대표 타입

### 현재 대표 타입
- `AuthAutoConfiguration`
- `AuthProperties`
- `AuthJwtProperties`
- `AuthSecurityAutoConfiguration`

### starter 원칙
- `ConditionalOnMissingBean`
- `ConditionalOnProperty`
- 얇은 bean wiring
- 구현체보다 설정과 조립에 집중

---

## 완료 기준

- JWT only 프로젝트가 이 starter 하나로 빠르게 붙을 수 있어야 한다.
- classpath 에 없는 session/hybrid 관련 bean 을 등록하지 않아야 한다.
- 설정 키와 기본값이 문서화되어야 한다.
