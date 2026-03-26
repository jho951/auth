# Module: auth-session-spring-boot-starter

> 상태: **현재 구현 없음**

---

## 목적

`auth-session-spring-boot-starter`는 세션 기반 프로젝트를 위한 조립 계층입니다.

현재 저장소에는 직접 대응되는 구현이 없으므로, 이 starter 역시 spec-first 로 설계합니다.

---

## 포함 범위

- session 관련 properties
- session provider bean 조립
- Spring Security 연결
- cookie/session extractor wiring
- 필요한 경우 기본 security chain 지원

---

## 제외 범위

- 세션 저장소의 실제 영속 구현
- JWT 토큰 구현
- OAuth2 이후 JWT 발급 흐름

---

## 현재 저장소에서의 매핑

현재 저장소에는 `auth-session-spring-boot-starter` 직접 매핑 대상이 없다.

향후 구현 시 `auth-session`과 `auth-spring`을 묶는 starter 로 추가한다.

---

## 의존 관계

- 필수 의존:
  - `auth-core`
  - `auth-session`
  - `auth-spring`
  - Spring Boot AutoConfigure

---

## 공개 API / 대표 타입

### 제안 타입
- `SessionAuthProperties`
- `SessionAuthAutoConfiguration`
- `SessionSecurityAutoConfiguration`

### 문서화 원칙
- session cookie 이름
- session principal 복원 전략
- 세션 미존재 시 처리 정책

---

## 완료 기준

- Session only 프로젝트가 JWT 모듈 없이 사용할 수 있어야 한다.
- starter 가 session mode 조립만 담당해야 한다.
- 설정 키와 bean 등록 규칙이 위키에 고정되어야 한다.
