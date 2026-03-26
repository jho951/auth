# Target Module Structure

## 목표 디렉터리

```text
auth
├─ auth-core
├─ auth-common-test
├─ auth-jwt
├─ auth-session
├─ auth-hybrid
├─ auth-spring
├─ auth-jwt-spring-boot-starter
├─ auth-session-spring-boot-starter
├─ auth-hybrid-spring-boot-starter
├─ samples
│  ├─ sample-jwt-api
│  ├─ sample-session-web
│  └─ sample-hybrid-sso
└─ docs
```

---

## 목표 모듈 설명

| 모듈 | 목적 |
| --- | --- |
| `auth-core` | 공통 모델, 예외, SPI, 유즈케이스, 공통 유틸 |
| `auth-common-test` | 테스트 픽스처, Fake 구현, 테스트 빌더 |
| `auth-jwt` | JWT 발급/검증과 claim 처리 |
| `auth-session` | 세션 인증용 추상화와 기본 구현 |
| `auth-hybrid` | JWT + Session 조합 전략 |
| `auth-spring` | Spring Web/Security 연동 |
| `auth-jwt-spring-boot-starter` | JWT starter 조립 |
| `auth-session-spring-boot-starter` | Session starter 조립 |
| `auth-hybrid-spring-boot-starter` | Hybrid + OAuth2 starter 조립 |
| `samples/*` | 예제 애플리케이션 |

---

## 모듈 선택 예시

### JWT only 프로젝트

```groovy
implementation("io.github.jho951:auth-core:<version>")
implementation("io.github.jho951:auth-jwt:<version>")
implementation("io.github.jho951:auth-spring:<version>")
implementation("io.github.jho951:auth-jwt-spring-boot-starter:<version>")
```

### Session only 프로젝트

```groovy
implementation("io.github.jho951:auth-core:<version>")
implementation("io.github.jho951:auth-session:<version>")
implementation("io.github.jho951:auth-spring:<version>")
implementation("io.github.jho951:auth-session-spring-boot-starter:<version>")
```

### Hybrid 프로젝트

```groovy
implementation("io.github.jho951:auth-core:<version>")
implementation("io.github.jho951:auth-jwt:<version>")
implementation("io.github.jho951:auth-session:<version>")
implementation("io.github.jho951:auth-hybrid:<version>")
implementation("io.github.jho951:auth-spring:<version>")
implementation("io.github.jho951:auth-hybrid-spring-boot-starter:<version>")
```

---

## 현재 구조에서의 매핑

| 현재 모듈/패키지 | 목표 모듈 |
| --- | --- |
| `contract` | `auth-core` |
| `spi` | `auth-core` |
| `common` | `auth-core` |
| `core` | `auth-core` |
| `support/jwt/*` | `auth-jwt` |
| `boot-support/config/security/*` | `auth-spring` + JWT starter |
| `boot-support/config/oauth/*` | `auth-hybrid-spring-boot-starter` |
| `boot-support/config/controller/*` | `auth-hybrid-spring-boot-starter` 또는 `auth-spring` |
| 테스트 공통 유틸(없음) | `auth-common-test` |
| 샘플 앱(없음) | `samples/*` |

---

## 핵심 리팩터링 규칙

1. `auth-core`는 가장 먼저 만든다.
2. 기존 `contract`, `spi`, `common`, `core`는 `auth-core`로 통합한다.
3. `auth-jwt`는 JWT 순수 구현만 가진다.
4. `auth-spring`은 Spring bridge만 가진다.
5. starter는 properties + auto-configuration + bean wiring만 가진다.
6. `auth-session`과 `auth-hybrid`는 spec-first로 시작할 수 있다.
7. 대규모 package rename은 첫 단계에서 강제하지 않는다.

---

## 열린 이슈

### `BCryptPasswordVerifier`
현재는 `support`에 있다.  
장기적으로는 다음 중 하나를 선택해야 한다.

- 임시로 legacy support에 유지
- 별도 `auth-password` 계층 도입
- starter 내부 지원 구현으로 한정

현재 목표 구조에는 별도 password 모듈이 없으므로, 이 이슈는 위키에 명시적으로 남겨야 한다.

### `InMemoryRefreshTokenStore`
현재는 `support`에 있다.  
이 구현체는 개발/테스트 환경에서는 유용하지만 저장소 구현이라는 별도 concern 이다.

선택지는 다음과 같다.

- 임시로 legacy support에 유지
- `auth-core`에서 test/demo 전용 구현으로 제한
- 향후 별도 store support 모듈 검토

---

## 목표 구조의 장점

- 필요한 구현만 classpath에 올릴 수 있다.
- starter가 얇아지고 조건 분기가 줄어든다.
- JWT/Session/Hybrid 경계가 분명해진다.
- 문서 구조가 모듈 구조와 자연스럽게 일치한다.
- OSS 사용자가 무엇을 선택해야 하는지 더 명확해진다.
