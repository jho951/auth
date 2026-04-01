# Docs

이 디렉터리는 **현재 체크인된 auth 저장소**를 기준으로 문서를 정리합니다.

핵심 원칙은 다음 두 가지입니다.

- 이 저장소는 **인증(Authentication)** 을 다룹니다.
- **인가(Authorization) / permission policy** 는 이 저장소 범위 밖입니다.

이 저장소는 **OSS auth** 레이어를 제공합니다.

또한 이 저장소는 현재 아래 **feature-oriented 모듈 구조**를 사용합니다.

- `auth-core`
- `auth-common-test`
- `auth-jwt`
- `auth-session`
- `auth-hybrid`
- `auth-spring`
- `auth-spring-boot-starter`

자세한 분리는 [architecture.md](./architecture.md)와 [modules.md](./modules.md)를 참고합니다.

## 먼저 읽기

1. [아키텍처 개요](./architecture.md)
2. [현재 저장소 상태](./current-repository-state.md)
3. [모듈 가이드](./modules.md)
4. [auth vs permission 경계](./auth-vs-permission-boundary.md)
5. [설정 레퍼런스](./configuration.md)
6. [API 가이드](./api.md)
7. [SPI/extension 가이드](./extension-guide.md)
8. [OAuth2 설계](./oauth2-design.md)
9. [샘플 사용법](./samples.md)
10. [보안 동작](./security.md)
11. [테스트/CI 가이드](./testing-and-ci.md)
12. [릴리즈 가이드](./release.md)
13. [트러블슈팅](./troubleshooting.md)

## 빠른 링크

- [RefreshCookieWriter 상세](./refresh-cookie-writer.md)
- [Redis RefreshTokenStore 가이드](./redis-refresh-token-store.md)

## GitHub Wiki에 그대로 옮길 때

이 디렉터리에는 Wiki용으로 쓸 수 있게 `Home.md`, `_Sidebar.md`도 함께 포함했습니다.
Repo `docs/` 디렉터리에서 계속 관리해도 되고, 필요하면 Wiki 저장소 루트로 복사하면 됩니다.
