# 현재 저장소 상태

이 문서는 **현재 체크인된 소스 트리** 기준 상태를 요약합니다.

## Gradle 서브프로젝트

`settings.gradle` 기준 현재 서브프로젝트는 다음 6개입니다.

- `contract`
- `spi`
- `common`
- `core`
- `support`
- `boot-support`

즉, 현재 저장소는 아직 `auth-core`, `auth-jwt`, `auth-session`, `auth-hybrid`, `auth-spring`, `*-starter` 구조로 분리되지 않았습니다.

## 모듈별 핵심 클래스

### `contract`

- `com.auth.api.model.Principal`
- `com.auth.api.model.User`
- `com.auth.api.model.Tokens`
- `com.auth.api.model.OAuth2UserIdentity`
- `com.auth.api.exception.AuthException`
- `com.auth.api.exception.AuthFailureReason`

### `spi`

- `com.auth.spi.UserFinder`
- `com.auth.spi.PasswordVerifier`
- `com.auth.spi.TokenService`
- `com.auth.spi.RefreshTokenStore`
- `com.auth.spi.OAuth2PrincipalResolver`

### `common`

- `com.auth.common.utils.Strings`
- `com.auth.common.utils.MoreObjects`

### `core`

- `com.auth.core.service.AuthService`

### `support`

- `com.auth.support.jwt.JwtTokenService`
- `com.auth.support.password.bcrypt.BCryptPasswordVerifier`
- `com.auth.support.refresh.memory.InMemoryRefreshTokenStore`

### `boot-support`

- `com.auth.config.AuthProperties`
- `com.auth.config.jwt.AuthJwtProperties`
- `com.auth.config.AuthAutoConfiguration`
- `com.auth.config.security.AuthSecurityAutoConfiguration`
- `com.auth.config.security.AuthOncePerRequestFilter`
- `com.auth.config.oauth.AuthOAuth2AutoConfiguration`
- `com.auth.config.oauth.OAuth2AuthenticationSuccessHandler`
- `com.auth.config.oauth.OAuth2AuthenticationFailureHandler`
- `com.auth.config.controller.RefreshCookieWriter`
- `com.auth.config.controller.RefreshTokenExtractor`

## 현재 의존 관계

- `contract` → `common`
- `spi` → `contract`
- `core` → `contract`, `spi`, `common`
- `support` → `spi`, `common`
- `boot-support` → `core`, `common`, `support`

## 현재 퍼블리싱 좌표에 대한 주의

루트 `build.gradle`은 `artifactId = project.name`을 사용합니다.
따라서 **현재 코드 기준 artifactId는 서브프로젝트 이름 그대로**입니다.

예:

- `io.github.jho951:contract:<version>`
- `io.github.jho951:spi:<version>`
- `io.github.jho951:common:<version>`
- `io.github.jho951:core:<version>`
- `io.github.jho951:support:<version>`
- `io.github.jho951:boot-support:<version>`

문서나 예제에서 `auth-core`, `auth-jwt-spring-boot-starter` 같은 좌표가 보인다면 그것은 **목표 구조 문맥**인지 먼저 확인해야 합니다.

## 현재 구현의 성격

- `boot-support`가 Spring Boot 조립, JWT 검증 필터, refresh cookie 보조 기능, OAuth2 성공 후 처리까지 함께 갖고 있습니다.
- `support`는 JWT/비밀번호/refresh 저장소의 기본 구현을 한곳에 모아 둡니다.
- `Principal`은 현재 `roles`와 `hasRole()` convenience API를 갖지만, 이 저장소는 최종 permission policy를 책임지지 않습니다.

## 현재 구조의 한계

- `support` 안에 JWT, password, refresh-store 구현이 함께 섞여 있어 concern 분리가 약합니다.
- `boot-support` 안에 JWT filter, OAuth2 handler, cookie helper가 함께 섞여 있어 feature-oriented 분리가 덜 되어 있습니다.
- 별도 `auth-session`, `auth-hybrid`, `auth-spring`, `*-starter` 모듈은 아직 없습니다.

## 향후 리팩터링 방향

향후에는 다음과 같은 목표 구조를 지향합니다.

- `auth-core`
- `auth-jwt`
- `auth-session`
- `auth-hybrid`
- `auth-spring`
- `auth-jwt-spring-boot-starter`
- `auth-session-spring-boot-starter`
- `auth-hybrid-spring-boot-starter`

자세한 내용은 [roadmap-target-module-structure.md](./roadmap-target-module-structure.md)를 참고합니다.
