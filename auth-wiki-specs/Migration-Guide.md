# Migration Guide

## 목표

현재 저장소의 6개 모듈 구조를 다음 구조로 점진적으로 전환한다.

```text
auth-core
auth-common-test
auth-jwt
auth-session
auth-hybrid
auth-spring
auth-jwt-spring-boot-starter
auth-session-spring-boot-starter
auth-hybrid-spring-boot-starter
samples/*
```

---

## 핵심 원칙

- **바로 rename 하지 않는다.**
- 먼저 `auth-core`로 통합한다.
- 그 다음 `support`와 `boot-support`를 역할별로 분해한다.
- package rename은 1차 목표가 아니다.
- 컴파일 가능한 상태를 매 단계 유지한다.

---

## 단계별 순서

### 1단계: 저장소 정리
- `build/`, `.gradle/`, `.idea/`, `__MACOSX`, `.DS_Store` 제거
- `grade.properties` → `gradle.properties` 로 이름 수정
- 루트 README 와 docs 에 현재 구조/목표 구조 병기

### 2단계: `auth-core` 생성
다음 모듈을 `auth-core`로 통합한다.

- `contract`
- `spi`
- `common`
- `core`

이 단계의 목표는 “공통 모델 + 예외 + SPI + 유즈케이스”를 하나의 모듈로 모으는 것이다.

### 3단계: `auth-jwt` 분리
다음 구현을 `auth-jwt`로 이동한다.

- `JwtTokenService`

### 4단계: `auth-spring` 분리
다음 Spring bridge 성격의 클래스를 `auth-spring`으로 분리한다.

- `AuthOncePerRequestFilter`
- 향후 `CurrentUser` 관련 타입
- 향후 SecurityContext bridge

### 5단계: starter 분리
현재 `boot-support`에서 다음을 분해한다.

#### `auth-jwt-spring-boot-starter`
- `AuthProperties`
- `AuthJwtProperties`
- `AuthAutoConfiguration`의 JWT bean 조립
- `AuthSecurityAutoConfiguration`의 JWT wiring

#### `auth-hybrid-spring-boot-starter`
- `AuthOAuth2AutoConfiguration`
- `OAuth2AuthenticationSuccessHandler`
- `OAuth2AuthenticationFailureHandler`
- `RefreshCookieWriter`
- `RefreshTokenExtractor`

### 6단계: spec-first module 생성
구현이 약한 다음 모듈은 일단 skeleton 부터 만든다.

- `auth-session`
- `auth-session-spring-boot-starter`
- `auth-hybrid`
- `auth-common-test`

### 7단계: sample 추가
- `sample-jwt-api`
- `sample-session-web`
- `sample-hybrid-sso`

---

## 현재 → 목표 매핑표

| 현재 | 목표 |
| --- | --- |
| `contract` | `auth-core` |
| `spi` | `auth-core` |
| `common` | `auth-core` |
| `core` | `auth-core` |
| `support/jwt` | `auth-jwt` |
| `boot-support/config/security` | `auth-spring` + JWT starter |
| `boot-support/config/oauth` | Hybrid starter |
| `boot-support/config/controller` | Hybrid starter 또는 Spring bridge |
| 테스트 유틸(분산) | `auth-common-test` |

---

## 조심할 점

### `BCryptPasswordVerifier`
현재 구조에서는 `support`에 있다.  
목표 구조에는 정확한 자리가 아직 정해지지 않았다.

실무적으로는 다음 중 하나를 선택한다.

- 임시 legacy support 유지
- 별도 `auth-password` 도입
- starter 내부 보조 구현으로 한정

### `InMemoryRefreshTokenStore`
현재 구조에서는 `support`에 있다.  
장기적으로는 test/demo 전용으로 유지할지, 별도 store support 로 뺄지 판단이 필요하다.

---

## 추천 커밋 단위

1. 저장소 청소
2. `auth-core` 통합
3. `auth-jwt` 분리
4. `auth-spring` 분리
5. `auth-jwt-spring-boot-starter` 추가
6. `auth-hybrid-spring-boot-starter` 추가
7. `auth-session` / `auth-common-test` skeleton 추가
8. `samples` 추가
9. 문서/위키 동기화

---

## 완료 기준

- `settings.gradle`이 목표 모듈 구조를 반영한다.
- 현재 기능이 유지된다.
- JWT only 프로젝트는 세션 모듈 없이 사용할 수 있다.
- `boot-support`와 `support`의 책임이 축소되거나 제거된다.
- 위키 문서와 실제 모듈 구조가 일치한다.
