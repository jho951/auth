# 테스트/CI 가이드

## 로컬 테스트 실행

전체 빌드:

```bash
./gradlew clean build
```

모듈 단위 테스트:

```bash
./gradlew :auth-core:test
./gradlew :auth-common-test:test
./gradlew :auth-jwt:test
./gradlew :auth-session:test
./gradlew :auth-hybrid:test
./gradlew :auth-spring:test
./gradlew :auth-spring-boot-starter:test
```

## 현재 테스트 범위

- `auth-core`
  - `AuthExceptionTest`, `OAuth2UserIdentityTest`, `PrincipalTest`, `TokensTest`, `UserTest`, `AuthServiceTest`, `StringsTest`
- `auth-jwt`
  - `JwtTokenServiceTest`
- `auth-session`
  - `DefaultSessionAuthenticationProviderTest`, `SessionServiceTest`
- `auth-hybrid`
  - `DefaultHybridAuthenticationProviderTest`
- `auth-spring-boot-starter`
  - `RefreshCookieWriterTest`, `OAuth2AuthenticationSuccessHandlerTest`
  - `AuthSessionAutoConfiguration` 관련 기본 wiring 테스트

## GitHub Actions

현재 워크플로우 파일:

- `.github/workflows/build.yml`
- `.github/workflows/publish.yml`
- `.github/workflows/discord-pr-notify.yml`

### `build.yml`

- 트리거: `main` 대상 PR, `main` push
- 수행: `./gradlew build --no-daemon --parallel --build-cache --stacktrace`
- 테스트 리포트 아티팩트 업로드 포함

### `publish.yml`

- 트리거: `v*` 태그 push, 수동 실행
- 수행:
  1. `./gradlew clean build`
  2. `./gradlew publish`
  3. 조건부로 Central Portal에 게시

### `discord-pr-notify.yml`

- PR opened / reopened 시 Discord webhook 알림

## 참고

CI와 문서는 소스 트리 기준으로 설명합니다. generated build 산출물은 문서 기준이 아닙니다.
