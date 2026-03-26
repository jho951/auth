# 아키텍처 개요

## 설계 목표

- 인증 도메인 로직을 프레임워크와 분리한다.
- Spring Boot 환경에서는 자동 설정으로 적용 부담을 줄인다.
- 사용자 저장소, 비밀번호 검증, 토큰 저장소, OAuth2 사용자 매핑을 SPI로 외부화한다.
- 리소스 권한 정책은 이 저장소에 넣지 않는다.

## 범위

### 이 저장소가 책임지는 것

- username/password 기반 로그인 유즈케이스
- 이미 인증된 사용자를 기준으로 access/refresh 발급
- refresh rotation
- access token 검증 후 Spring Security 컨텍스트 연결
- OAuth2 로그인 성공 후 내부 principal 매핑과 토큰 발급 연결

### 이 저장소가 책임지지 않는 것

- 문서/워크스페이스/블록 단위 permission decision
- owner/editor/viewer 해석
- 리소스 소유권 검사
- ABAC/RBAC policy engine
- 도메인 권한 서비스

자세한 구분은 [auth-vs-permission-boundary.md](./auth-vs-permission-boundary.md)를 참고합니다.

## 현재 구현 구조

현재 구현은 아래 layered layout을 사용합니다.

- `contract` — `Principal`, `User`, `Tokens`, `OAuth2UserIdentity`, `AuthException`, `AuthFailureReason`
- `spi` — `UserFinder`, `PasswordVerifier`, `TokenService`, `RefreshTokenStore`, `OAuth2PrincipalResolver`
- `common` — `Strings`, `MoreObjects`
- `core` — `AuthService`
- `support` — `JwtTokenService`, `BCryptPasswordVerifier`, `InMemoryRefreshTokenStore`
- `boot-support` — `AuthAutoConfiguration`, `AuthSecurityAutoConfiguration`, `AuthOAuth2AutoConfiguration`, `AuthOncePerRequestFilter`, `RefreshCookieWriter`, `RefreshTokenExtractor`

## 요청 흐름

### 1) username/password 로그인

1. 애플리케이션이 `/login` 같은 엔드포인트를 직접 구현한다.
2. 컨트롤러가 `AuthService.login(username, password)`를 호출한다.
3. `AuthService`가 `UserFinder`로 사용자를 찾고 `PasswordVerifier`로 비밀번호를 검증한다.
4. `TokenService`가 access/refresh token을 발급한다.
5. `RefreshTokenStore`가 refresh token을 저장한다.
6. 필요하면 `RefreshCookieWriter`가 refresh cookie를 응답에 추가한다.

### 2) refresh rotation

1. 애플리케이션이 `/refresh` 엔드포인트를 직접 구현한다.
2. 필요하면 `RefreshTokenExtractor`로 쿠키에서 refresh token을 읽는다.
3. `AuthService.refresh(refreshToken)`을 호출한다.
4. `TokenService.verifyRefreshToken`으로 refresh token을 검증한다.
5. `RefreshTokenStore.exists`로 서버 저장소 기준 유효성을 확인한다.
6. 기존 refresh token을 폐기하고 새 access/refresh pair를 발급한다.
7. 필요하면 새 refresh cookie를 다시 기록한다.

### 3) 로그아웃

1. 애플리케이션이 `/logout` 엔드포인트를 직접 구현한다.
2. 필요하면 `RefreshTokenExtractor`로 refresh token을 읽는다.
3. `AuthService.logout(refreshToken)`을 호출한다.
4. `RefreshTokenStore.revoke`로 서버 저장소의 refresh token을 폐기한다.
5. 필요하면 `RefreshCookieWriter.clear`로 쿠키를 만료 처리한다.

### 4) access token 인증 필터

1. `AuthOncePerRequestFilter`가 `Authorization` 헤더를 읽는다.
2. `auth.bearer-prefix`와 일치하면 토큰 본문을 추출한다.
3. `TokenService.verifyAccessToken`으로 `Principal`을 복원한다.
4. 현재 구현에서는 `Principal.roles`를 `SimpleGrantedAuthority`로 변환해 `SecurityContext`에 넣는다.
5. 이후 세부 permission 판단은 애플리케이션 또는 별도 permission 계층이 수행한다.

### 5) OAuth2 로그인 성공 흐름

1. 클라이언트가 `/oauth2/authorization/{registrationId}`로 이동한다.
2. Spring Security OAuth2 Client가 Provider 인증을 수행한다.
3. `OAuth2AuthenticationSuccessHandler`가 `OAuth2AuthenticatedPrincipal`을 `OAuth2UserIdentity`로 바꾼다.
4. `OAuth2PrincipalResolver`가 내부 `Principal`을 반환한다.
5. `AuthService.login(principal)`이 access/refresh token을 발급한다.
6. 핸들러가 JSON body와 refresh cookie를 응답으로 쓴다.

## Spring Boot 자동 구성 진입점

`boot-support`는 현재 다음 자동 구성을 등록합니다.

- `com.auth.config.AuthAutoConfiguration`
- `com.auth.config.security.AuthSecurityAutoConfiguration`
- `com.auth.config.oauth.AuthOAuth2AutoConfiguration`

등록 파일:

- `boot-support/src/main/resources/META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports`

## 현재 구조와 목표 구조의 차이

문서 전반에서 `auth-core`, `auth-jwt`, `auth-session`, `auth-hybrid`, `auth-spring`, `*-starter` 구조가 언급될 수 있습니다. 이 구조는 **현재 구현이 아니라 향후 목표**입니다.
현재 저장소를 이해하려면 [current-repository-state.md](./current-repository-state.md)를, 향후 분리 방향은 [roadmap-target-module-structure.md](./roadmap-target-module-structure.md)를 참고하면 됩니다.
