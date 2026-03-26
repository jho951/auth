# 마이그레이션 가이드

이 문서는 현재 layered 구조에서 목표 feature-oriented 구조로 이동할 때의 **대응 관계**를 정리합니다.

> 주의: 이 문서는 목적지 설명입니다. 현재 저장소는 아직 이 구조로 분리되어 있지 않습니다.

## 현재 → 목표 매핑

| 현재 | 목표 |
|---|---|
| `contract` | `auth-core` 일부 |
| `spi` | `auth-core` 일부 |
| `common` | `auth-core` 일부 또는 별도 util 정리 |
| `core` | `auth-core` 일부 |
| `support/src/main/java/com/auth/support/jwt/*` | `auth-jwt` |
| `support/src/main/java/com/auth/support/password/*` | 별도 support 또는 future `auth-password` 후보 |
| `support/src/main/java/com/auth/support/refresh/*` | test/support helper 또는 future token-store split 후보 |
| `boot-support`의 Spring Security 브릿지 | `auth-spring` |
| `boot-support`의 JWT 자동 구성 | `auth-jwt-spring-boot-starter` |
| `boot-support`의 OAuth2 success/failure handler | `auth-hybrid-spring-boot-starter` |

## 권장 순서

1. `contract`, `spi`, `common`, `core`를 먼저 `auth-core`로 통합
2. `support`에서 JWT 구현을 `auth-jwt`로 분리
3. `boot-support`에서 공통 Spring 브릿지를 `auth-spring`으로 분리
4. JWT 자동 구성을 `auth-jwt-spring-boot-starter`로 분리
5. OAuth2 + refresh cookie 흐름을 `auth-hybrid-spring-boot-starter`로 분리
6. 필요하면 `auth-session`, `auth-common-test`, `samples`를 추가

## 현재 문서와의 관계

현재 docs는 **현재 구현 문서**와 **로드맵 문서**를 분리했습니다.
리팩터링이 실제로 끝나기 전까지는 roadmap 문서를 현재 구현 문서처럼 읽으면 안 됩니다.
