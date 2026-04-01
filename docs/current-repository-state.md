# 현재 저장소 상태

이 문서는 **현재 체크인된 소스 트리** 기준 상태를 요약합니다.

## Gradle 서브프로젝트

`settings.gradle` 기준 현재 주요 서브프로젝트는 다음과 같습니다.

- `auth-core`
- `auth-common-test`
- `auth-jwt`
- `auth-session`
- `auth-hybrid`
- `auth-spring`
- `auth-spring-boot-starter`
- `samples/sample-jwt-api`
- `samples/sample-session-web`
- `samples/sample-hybrid-sso`

즉, 현재 저장소는 이미 feature-oriented 구조로 분리되어 있습니다.

## 모듈별 핵심 클래스

### `auth-core`

- `com.auth.api.model.Principal`
- `com.auth.api.model.User`
- `com.auth.api.model.Tokens`
- `com.auth.api.model.OAuth2UserIdentity`
- `com.auth.api.exception.AuthException`
- `com.auth.api.exception.AuthFailureReason`
- `com.auth.spi.UserFinder`
- `com.auth.spi.PasswordVerifier`
- `com.auth.spi.TokenService`
- `com.auth.spi.RefreshTokenStore`
- `com.auth.spi.OAuth2PrincipalResolver`
- `com.auth.common.utils.Strings`
- `com.auth.common.utils.MoreObjects`
- `com.auth.core.service.AuthService`

### `auth-common-test`

- `com.auth.test.AuthTestFixtures`
- `com.auth.support.refresh.memory.InMemoryRefreshTokenStore`

### `auth-jwt`

- `com.auth.support.jwt.JwtTokenService`

### `auth-session`

- `com.auth.session.SessionStore`
- `com.auth.session.SessionService`
- `com.auth.session.SessionPrincipalMapper`
- `com.auth.session.SessionCookieExtractor`
- `com.auth.session.SessionAuthenticationProvider`
- `com.auth.session.SessionIdGenerator`
- `com.auth.session.DefaultSessionAuthenticationProvider`
- `com.auth.session.DefaultSessionCookieExtractor`
- `com.auth.session.SimpleSessionStore`
- `com.auth.session.IdentitySessionPrincipalMapper`
- `com.auth.session.security.SessionAuthenticationFilter`

### `auth-hybrid`

- `com.auth.hybrid.HybridAuthenticationContext`
- `com.auth.hybrid.HybridAuthenticationProvider`
- `com.auth.hybrid.CompositeAuthenticationProvider`
- `com.auth.hybrid.DefaultHybridAuthenticationProvider`

### `auth-spring`

- `com.auth.config.AuthProperties`

### `auth-spring-boot-starter`

- `com.auth.config.jwt.AuthJwtProperties`
- `com.auth.config.AuthAutoConfiguration`
- `com.auth.config.security.AuthSecurityAutoConfiguration`
- `com.auth.config.security.AuthOncePerRequestFilter`
- `com.auth.config.controller.RefreshTokenExtractor`
- `com.auth.session.config.AuthSessionAutoConfiguration`
- `com.auth.session.config.AuthSessionProperties`
- `com.auth.config.hybrid.AuthHybridAutoConfiguration`
- `com.auth.config.hybrid.AuthHybridCookieAutoConfiguration`
- `com.auth.config.oauth.AuthOAuth2AutoConfiguration`
- `com.auth.support.password.bcrypt.BCryptPasswordVerifier`
- `com.auth.config.oauth.OAuth2AuthenticationSuccessHandler`
- `com.auth.config.oauth.OAuth2AuthenticationFailureHandler`
- `com.auth.config.controller.RefreshCookieWriter`

## 현재 의존 관계

- `auth-common-test` → `auth-core`
- `auth-jwt` → `auth-core`
- `auth-session` → `auth-core`
- `auth-hybrid` → `auth-core`, `auth-session`, `auth-jwt`
- `auth-spring` → `auth-core`
- `auth-spring-boot-starter` → `auth-core`, `auth-jwt`, `auth-session`, `auth-hybrid`, `auth-spring`, `auth-common-test`

## 현재 퍼블리싱 좌표에 대한 주의

루트 `build.gradle`은 `artifactId = project.name`을 사용합니다.
따라서 **현재 코드 기준 artifactId는 서브프로젝트 이름 그대로**입니다.

예:

- `io.github.jho951:auth-core:<version>`
- `io.github.jho951:auth-common-test:<version>`
- `io.github.jho951:auth-jwt:<version>`
- `io.github.jho951:auth-session:<version>`
- `io.github.jho951:auth-hybrid:<version>`
- `io.github.jho951:auth-spring:<version>`
- `io.github.jho951:auth-spring-boot-starter:<version>`

## 현재 구현의 성격

- `auth-spring-boot-starter`가 JWT 검증 필터, 세션 자동 구성, hybrid 조합, OAuth2 성공/실패 처리, refresh cookie writing을 담당합니다.
- `Principal`은 `authorities`와 `attributes`를 운반하지만, 최종 permission policy를 책임지지 않습니다.

## 현재 구조의 한계

- 기본 refresh token 저장소는 테스트용 in-memory 구현입니다.
- 운영 환경에서는 `RefreshTokenStore`를 Redis/DB 등으로 교체하는 것이 일반적입니다.
- `auth-session`과 `auth-hybrid`는 조합 전략을 제공하지만, 실제 사용자/계정 연결 정책은 애플리케이션이 소유해야 합니다.
