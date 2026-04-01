# 모듈 가이드

## 공통 원리

이 저장소의 모듈은 다음 원리를 공유합니다.

1. 인증 도메인 모델은 공통으로 재사용한다.
2. 확장 포인트는 SPI로 분리한다.
3. 기본 구현은 제공하되, 서비스별 정책은 애플리케이션이 교체한다.
4. 프레임워크 연동은 starter 또는 bridge 모듈이 맡는다.

## 현재 모듈 구성

현재 저장소는 아래 모듈들을 사용합니다.

| 모듈 | 역할 |
|---|---|
| `auth-core` | 공개 모델, 예외, SPI, 공통 유틸, `AuthService` |
| `auth-common-test` | 테스트 픽스처와 테스트용 in-memory helper |
| `auth-jwt` | JWT 발급/검증 구현 |
| `auth-session` | 세션 저장소와 세션 인증 조합 |
| `auth-hybrid` | JWT/세션 조합 전략 |
| `auth-spring` | Spring 연동용 공통 설정 바인딩 |
| `auth-spring-boot-starter` | JWT, 세션, hybrid, OAuth2 자동 구성과 Security bridge |

이 층에는 서비스 이름이나 사내 고유 명칭이 들어가면 안 됩니다.

## 사용 시나리오별 권장 의존성

### 1. 순수 Java로 인증 유즈케이스만 쓰는 경우

최소 구성:

- `auth-core`
- 애플리케이션 구현: `UserFinder`

설명:

- `AuthService`는 `auth-core`에 있습니다.
- Spring Boot 없이도 사용할 수 있습니다.

### 2. Spring Boot에서 인증 자동 설정을 함께 쓰는 경우

최소 구성:

- `auth-core`
- `auth-jwt`
- `auth-session`
- `auth-hybrid`
- `auth-spring`
- `auth-spring-boot-starter`
- `spring-boot-starter-web`
- `spring-boot-starter-security`

설명:

- `auth-spring-boot-starter`가 `AuthAutoConfiguration`, `AuthSecurityAutoConfiguration`, `AuthSessionAutoConfiguration`, `AuthHybridAutoConfiguration`, `AuthHybridCookieAutoConfiguration`, `AuthOAuth2AutoConfiguration`을 등록합니다.
- 기본 `TokenService`, `PasswordVerifier`, `RefreshTokenStore`는 `@ConditionalOnMissingBean`으로 자동 제공됩니다.
- 세션, hybrid, OAuth2 관련 자동 구성도 같은 starter에서 함께 제공됩니다.

### 3. 세션 기반 인증을 쓰는 경우

최소 구성:

- `auth-core`
- `auth-session`
- `auth-spring`
- `auth-spring-boot-starter`
- `spring-boot-starter-web`
- `spring-boot-starter-security`

설명:

- `auth-spring-boot-starter`가 `AuthSessionAutoConfiguration`과 관련 기본 구성을 등록합니다.
- 세션 저장소와 principal mapper는 애플리케이션이 필요에 맞게 교체할 수 있습니다.

### 4. OAuth2 로그인 성공 후 내부 principal 매핑과 JWT 발급까지 연결하는 경우

최소 구성:

- `auth-core`
- `auth-spring-boot-starter`
- `spring-boot-starter-security`
- `spring-boot-starter-oauth2-client`
- 애플리케이션 구현: `OAuth2PrincipalResolver`

설명:

- `auth-spring-boot-starter`의 `AuthOAuth2AutoConfiguration`이 success/failure handler를 등록합니다.
- Provider 설정은 애플리케이션의 `spring.security.oauth2.client.*`가 담당합니다.

### 5. 테스트 공용 자산이 필요한 경우

최소 구성:

- `auth-common-test`

설명:

- 여러 모듈의 통합 테스트에서 `AuthTestFixtures`와 `InMemoryRefreshTokenStore`를 재사용할 수 있습니다.

## 현재 구조와 문서

- 세부 구조는 [current-repository-state.md](./current-repository-state.md)를 참고합니다.
- 요청 흐름은 [architecture.md](./architecture.md)를 참고합니다.
