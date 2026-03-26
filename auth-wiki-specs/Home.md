# Auth Wiki

## 개요

이 위키는 `auth` 저장소의 **현재 구현 상태**와 **목표 멀티모듈 구조**를 함께 관리하기 위한 명세 문서입니다.

중요한 전제는 다음 두 가지입니다.

1. **현재 저장소의 실제 구조**
   - `contract`
   - `spi`
   - `common`
   - `core`
   - `support`
   - `boot-support`

2. **목표 구조**
   - `auth-core`
   - `auth-common-test`
   - `auth-jwt`
   - `auth-session`
   - `auth-hybrid`
   - `auth-spring`
   - `auth-jwt-spring-boot-starter`
   - `auth-session-spring-boot-starter`
   - `auth-hybrid-spring-boot-starter`
   - `samples/*`

즉, 이 위키는 **“현재 레포 설명서”이면서 동시에 “리팩터링 타깃 명세”** 입니다.

---

## 설계 목표

- 인증 도메인 로직을 프레임워크와 저장소 구현에서 분리
- JWT 전용, Session 전용, Hybrid 프로젝트를 모두 수용
- 공통 코어는 얇게 유지하고 구현체/조립 계층은 선택적으로 가져가게 구성
- Spring Boot에서는 starter로 빠르게 연결
- GitHub Wiki만 읽어도 각 모듈의 책임과 경계를 이해할 수 있게 정리

---

## 현재 구조 vs 목표 구조

| 구분 | 현재 | 목표 |
| --- | --- | --- |
| 코어 모델/예외 | `contract` | `auth-core` |
| SPI | `spi` | `auth-core` |
| 공통 유즈케이스 | `core` | `auth-core` |
| 유틸 | `common` | `auth-core` |
| JWT 구현 | `support` 일부 | `auth-jwt` |
| Spring Boot 자동설정 | `boot-support` | `auth-spring` + 각 starter |
| Session 인증 | 별도 모듈 없음 | `auth-session` |
| Hybrid 조합 | `boot-support` 내부에 일부 혼재 | `auth-hybrid` |
| 테스트 픽스처 | 분산 | `auth-common-test` |
| 샘플 애플리케이션 | 없음 | `samples/*` |

---

## 권장 읽기 순서

1. [Architecture](Architecture)
2. [Current Repository State](Current-Repository-State)
3. [Target Module Structure](Target-Module-Structure)
4. [Module: auth-core](Module-Auth-Core)
5. [Module: auth-jwt](Module-Auth-JWT)
6. [Module: auth-session](Module-Auth-Session)
7. [Module: auth-hybrid](Module-Auth-Hybrid)
8. [Module: auth-spring](Module-Auth-Spring)
9. [Configuration Reference](Configuration-Reference)
10. [SPI and Extension Points](SPI-and-Extension-Points)
11. [Migration Guide](Migration-Guide)

---

## 현재 저장소에서 확인된 핵심 클래스

- `com.auth.core.service.AuthService`
- `com.auth.support.jwt.JwtTokenService`
- `com.auth.support.password.bcrypt.BCryptPasswordVerifier`
- `com.auth.support.refresh.memory.InMemoryRefreshTokenStore`
- `com.auth.config.AuthAutoConfiguration`
- `com.auth.config.security.AuthOncePerRequestFilter`
- `com.auth.config.security.AuthSecurityAutoConfiguration`
- `com.auth.config.oauth.AuthOAuth2AutoConfiguration`
- `com.auth.config.controller.RefreshCookieWriter`
- `com.auth.config.controller.RefreshTokenExtractor`

---

## 이 위키에서 지키는 규칙

- **현재 구현된 내용**과 **앞으로 만들 목표 구조**를 구분해서 표기한다.
- 아직 없는 모듈(`auth-session`, `auth-hybrid`, `auth-common-test`)은 **spec-first** 로 문서화한다.
- 모듈 책임은 “포함 범위 / 제외 범위 / 공개 API / 의존 방향”으로 명시한다.
- 서비스 애플리케이션이 책임지는 것(로그인 UI, 회원가입, 최종 권한 정책 등)은 auth 모듈 책임에서 제외한다.
