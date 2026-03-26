# Docs

이 디렉터리는 **현재 구현된 auth 저장소**를 기준으로 문서를 정리합니다.
핵심 원칙은 다음 두 가지입니다.

- 이 저장소는 **인증(Authentication)** 을 다룹니다.
- **인가(Authorization) / permission policy** 는 이 저장소 범위 밖입니다.

또한 이 저장소는 현재 **layered module 구조**를 사용합니다.

- `contract`
- `spi`
- `common`
- `core`
- `support`
- `boot-support`

`auth-core`, `auth-jwt`, `auth-session`, `auth-hybrid`, `auth-spring`, `*-starter` 형태의 구조는 **미래 리팩터링 목표**이며, 현재 구현이 아닙니다.

## 먼저 읽기

1. [아키텍처 개요](./architecture.md)
2. [현재 저장소 상태](./current-repository-state.md)
3. [모듈 가이드](./modules.md)
4. [auth vs permission 경계](./auth-vs-permission-boundary.md)
5. [설정 레퍼런스](./configuration.md)
6. [API 가이드](./api.md)
7. [SPI 확장 가이드](./extension-guide.md)
8. [OAuth2 설계](./oauth2-design.md)
9. [보안 동작](./security.md)
10. [로드맵: 목표 모듈 구조](./roadmap-target-module-structure.md)
11. [마이그레이션 가이드](./migration-guide.md)

## 현재 구현 문서

- [아키텍처 개요](./architecture.md)
- [현재 저장소 상태](./current-repository-state.md)
- [모듈 가이드](./modules.md)
- [Module: contract](./module-contract.md)
- [Module: spi](./module-spi.md)
- [Module: common](./module-common.md)
- [Module: core](./module-core.md)
- [Module: support](./module-support.md)
- [Module: boot-support](./module-boot-support.md)
- [auth vs permission 경계](./auth-vs-permission-boundary.md)
- [설정 레퍼런스](./configuration.md)
- [API 가이드](./api.md)
- [SPI 확장 가이드](./extension-guide.md)
- [OAuth2 설계](./oauth2-design.md)
- [Redis RefreshTokenStore 가이드](./redis-refresh-token-store.md)
- [RefreshCookieWriter 상세](./refresh-cookie-writer.md)
- [보안 동작](./security.md)
- [테스트/CI 가이드](./testing-and-ci.md)
- [릴리즈 가이드](./release.md)
- [트러블슈팅](./troubleshooting.md)

## OAuth2 빠른 시작

- [Google OAuth2 Quickstart](./oauth2-google-quickstart.md)
- [GitHub OAuth2 Quickstart](./oauth2-github-quickstart.md)
- [Kakao OAuth2 Quickstart](./oauth2-kakao-quickstart.md)

## 로드맵 문서

- [로드맵: 목표 모듈 구조](./roadmap-target-module-structure.md)
- [마이그레이션 가이드](./migration-guide.md)

## GitHub Wiki에 그대로 옮길 때

이 ZIP에는 Wiki용으로 쓸 수 있게 `Home.md`, `_Sidebar.md`도 함께 포함했습니다.
Repo `docs/` 디렉터리에서 계속 관리해도 되고, 필요하면 Wiki 저장소 루트로 복사하면 됩니다.
