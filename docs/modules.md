# 모듈 가이드

## 현재 모듈 구성

현재 저장소는 아래 **6개 서브프로젝트**를 사용합니다.

| 모듈 | 역할 |
|---|---|
| `contract` | 공개 모델과 예외 |
| `spi` | 외부 구현체가 채우는 확장 포트 |
| `common` | 공용 유틸 |
| `core` | 인증 도메인 로직 |
| `support` | 기본 구현체 모음 |
| `boot-support` | Spring Boot 자동 구성과 보안 연결 |

## 사용 시나리오별 권장 의존성

### 1. 순수 Java로 인증 유즈케이스만 쓰는 경우

최소 구성:

- `core`
- `support`
- 애플리케이션 구현: `UserFinder`

설명:

- `AuthService`는 `core`에 있습니다.
- 기본 JWT/비밀번호/refresh 저장소 구현은 `support`에 있습니다.
- Spring Boot 없이도 사용할 수 있습니다.

### 2. Spring Boot에서 JWT 인증 필터와 자동 설정을 함께 쓰는 경우

최소 구성:

- `boot-support`
- `spring-boot-starter-web`
- `spring-boot-starter-security`

설명:

- `boot-support`가 `AuthAutoConfiguration`, `AuthSecurityAutoConfiguration`, `AuthOncePerRequestFilter`를 등록합니다.
- 기본 `TokenService`, `PasswordVerifier`, `RefreshTokenStore`는 `@ConditionalOnMissingBean`으로 자동 제공됩니다.

### 3. OAuth2 로그인 성공 후 내부 principal 매핑과 JWT 발급까지 연결하는 경우

최소 구성:

- `boot-support`
- `spring-boot-starter-security`
- `spring-boot-starter-oauth2-client`
- 애플리케이션 구현: `OAuth2PrincipalResolver`

설명:

- `boot-support`의 `AuthOAuth2AutoConfiguration`이 success/failure handler를 등록합니다.
- Provider 설정은 애플리케이션의 `spring.security.oauth2.client.*`가 담당합니다.

## 모듈별 상세 문서

- [Module: contract](./module-contract.md)
- [Module: spi](./module-spi.md)
- [Module: common](./module-common.md)
- [Module: core](./module-core.md)
- [Module: support](./module-support.md)
- [Module: boot-support](./module-boot-support.md)

## 현재 구조와 목표 구조

현재는 layered 구조이지만, 장기적으로는 feature-oriented 구조로 분리하는 것이 목표입니다.

- 현재: `contract` / `spi` / `common` / `core` / `support` / `boot-support`
- 목표: `auth-core` / `auth-jwt` / `auth-session` / `auth-hybrid` / `auth-spring` / `*-starter`

현재 구현이 아닌 목표 구조 설명은 [roadmap-target-module-structure.md](./roadmap-target-module-structure.md)에 분리해 두었습니다.
