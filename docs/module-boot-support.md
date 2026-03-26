# Module: boot-support

## 역할

`boot-support`는 현재 저장소의 **Spring Boot 조립 계층**입니다.

현재 구조에서는 아래 concern이 이 모듈 하나에 모여 있습니다.

- 인증 관련 bean 자동 등록
- 기본 `SecurityFilterChain`
- access token 검증 필터
- refresh cookie helper
- refresh cookie extractor
- OAuth2 로그인 성공/실패 처리

## 핵심 클래스

### 프로퍼티

- `com.auth.config.AuthProperties`
- `com.auth.config.jwt.AuthJwtProperties`

### 자동 구성

- `com.auth.config.AuthAutoConfiguration`
- `com.auth.config.security.AuthSecurityAutoConfiguration`
- `com.auth.config.oauth.AuthOAuth2AutoConfiguration`

### 웹 보조 기능

- `com.auth.config.security.AuthOncePerRequestFilter`
- `com.auth.config.controller.RefreshCookieWriter`
- `com.auth.config.controller.RefreshTokenExtractor`

### OAuth2 성공/실패 핸들러

- `com.auth.config.oauth.OAuth2AuthenticationSuccessHandler`
- `com.auth.config.oauth.OAuth2AuthenticationFailureHandler`

## 자동 등록 동작

### `AuthAutoConfiguration`

기본적으로 다음 bean을 조립합니다.

- `RefreshTokenStore` (`InMemoryRefreshTokenStore`)
- `TokenService` (`JwtTokenService`, `auth.jwt.secret`가 있을 때)
- `PasswordVerifier` (`BCryptPasswordVerifier`)
- `AuthService`
- `AuthOncePerRequestFilter`
- `RefreshCookieWriter`
- `RefreshTokenExtractor`

대부분 `@ConditionalOnMissingBean`을 사용하므로 애플리케이션에서 같은 타입 빈을 먼저 등록하면 교체할 수 있습니다.

### `AuthSecurityAutoConfiguration`

기본 보안 체인을 제공합니다.

- stateless session policy
- csrf / formLogin / httpBasic / logout 비활성화
- OAuth2 시작/콜백 경로 permitAll
- 나머지 요청 authenticated
- 401 JSON: `{"message":"UNAUTHORIZED"}`
- 403 JSON: `{"message":"FORBIDDEN"}`

### `AuthOAuth2AutoConfiguration`

다음 조건에서 OAuth2 성공/실패 핸들러를 등록합니다.

- `spring-security-oauth2-client`가 classpath에 있음
- `OAuth2PrincipalResolver` bean이 있음
- `auth.oauth2.enabled=true`

## 현재 구조상의 한계

`boot-support`는 현재 너무 많은 역할을 한곳에 갖고 있습니다.
장기적으로는 `auth-spring`, `auth-jwt-spring-boot-starter`, `auth-hybrid-spring-boot-starter` 같은 구조로 분리하는 것이 더 좋습니다.

이 내용은 [roadmap-target-module-structure.md](./roadmap-target-module-structure.md)를 참고합니다.
