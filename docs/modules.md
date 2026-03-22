# 모듈 가이드

## 멀티 모듈 구성

- 루트 설정: `settings.gradle`
- 포함 모듈: `contract`, `spi`, `core`, `boot-support`, `support`, `common`

## 모듈별 책임

## `contract`

- 위치: `contract/src/main/java/com/auth/api`
- 책임:
  - 예외 타입: `AuthException`
  - 에러 코드: `ErrorCode`
  - 핵심 모델: `User`, `Principal`, `Tokens`
- 특징:
  - 다른 모듈/외부 애플리케이션에서 공통으로 사용

## `spi`

- 위치: `spi/src/main/java/com/auth/spi`
- 책임:
  - 확장 포트 정의
  - `UserFinder`
  - `PasswordVerifier`
  - `TokenService`
  - `RefreshTokenStore`
  - `OAuth2PrincipalResolver`
- 특징:
  - 코어는 구현이 아닌 인터페이스에만 의존

## `core`

- 위치: `core/src/main/java/com/auth/core/service/AuthService.java`
- 책임:
  - 로그인/재발급/로그아웃 유즈케이스
  - refresh rotation 정책 적용
  - 토큰 저장소와 토큰 서비스 협력

## `boot-support`

- 위치: `boot-support/src/main/java/com/auth/config`
- 책임:
  - Spring Boot 자동 설정
  - `AuthService`, 지원 모듈 구현체, 필터, 쿠키 유틸 조립
  - OAuth2 로그인 성공/실패 핸들러
  - 보안 필터/기본 SecurityFilterChain
  - refresh cookie 처리
  - `support` 구현체를 Spring Bean으로 조립

## `support`

- 위치: `support/src/main/java/com/auth/support`
- 책임:
  - 순수 Java 기반 `JwtTokenService` 제공
  - 순수 Java 기반 `BCryptPasswordVerifier` 제공
  - 메모리 기반 `InMemoryRefreshTokenStore` 제공
  - 기본 `TokenService`, `PasswordVerifier`, `RefreshTokenStore` 구현 담당

## `common`

- 위치: `common/src/main/java/com/auth/common/utils/Strings.java`
- 책임:
  - 문자열/널 검증 유틸 제공

## 의존 관계

- `core` -> `contract`, `spi`, `common`
- `spi` -> `contract`
- `support` -> `spi`, `common`
- `boot-support` -> `core`, `common`, `support` (실행 시 Spring Web/Security 의존)
- `contract` -> `common`

## artifact 좌표

루트 `build.gradle`에서 publish 시 artifactId는 `auth-{module}` 규칙을 사용합니다.

예:
- `io.github.jho951:auth-contract:1.0.8`
- `io.github.jho951:auth-core:1.0.8`
- `io.github.jho951:auth-spi:1.0.8`
- `io.github.jho951:auth-boot-support:1.0.8`
- `io.github.jho951:auth-support:1.0.8`
- `io.github.jho951:auth-common:1.0.8`
