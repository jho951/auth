# Auth Module Wiki

이 문서는 **현재 구현된 auth 저장소** 기준의 위키 홈입니다.

## 현재 구조

현재 저장소는 feature-oriented 구조가 아니라 **layered structure** 를 사용합니다.

- `contract` — 공개 모델/예외
- `spi` — 확장 포트
- `common` — 공용 유틸
- `core` — 인증 도메인 로직
- `support` — 기본 구현체
- `boot-support` — Spring Boot 조립 계층

## 범위

- **포함**: 인증, JWT 발급/검증, refresh rotation, Spring Security 연동, OAuth2 성공 후 내부 principal 매핑 연결
- **제외**: 리소스 권한 판단, owner/editor/viewer 해석, 문서/워크스페이스/블록 permission policy

## 먼저 볼 문서

- [Architecture](./architecture.md)
- [Current Repository State](./current-repository-state.md)
- [Modules](./modules.md)
- [Auth vs Permission Boundary](./auth-vs-permission-boundary.md)
- [Configuration](./configuration.md)
- [API](./api.md)
- [Extension Guide](./extension-guide.md)

## 로드맵

향후에는 `auth-core`, `auth-jwt`, `auth-session`, `auth-hybrid`, `auth-spring`, `*-starter`로 분리하는 방향을 목표로 합니다.
이 내용은 [roadmap-target-module-structure.md](./roadmap-target-module-structure.md)에 별도로 정리합니다.
