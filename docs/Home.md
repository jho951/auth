# Auth Module Wiki

이 문서는 **현재 구현된 auth 저장소** 기준의 위키 홈입니다.

## 현재 구조

현재 저장소는 아래 **feature-oriented 모듈 구조**를 사용합니다.

- `auth-core` - 인증 도메인, 공개 모델, SPI, `AuthService`
- `auth-common-test` - 테스트 픽스처와 in-memory helper
- `auth-jwt` - JWT 토큰 발급/검증
- `auth-session` - 세션 저장소와 세션 인증 필터
- `auth-hybrid` - JWT/세션 조합 전략
- `auth-spring` - Spring 연동용 공통 설정
- `auth-spring-boot-starter` - JWT, 세션, hybrid, OAuth2 자동 구성

OSS auth는 공통 원리, 확장 포인트, 기본 구현, 프레임워크 연동 책임을 제공합니다.

## 범위

- **포함**: 인증, JWT 발급/검증, session 기반 인증, refresh rotation, Spring Security 연동, OAuth2 성공 후 내부 principal 매핑 연결
- **제외**: 리소스 권한 판단, owner/editor/viewer 해석, 문서/워크스페이스/블록 permission policy

## 먼저 볼 문서

- [Architecture](./architecture.md)
- [Current Repository State](./current-repository-state.md)
- [Modules](./modules.md)
- [Auth vs Permission Boundary](./auth-vs-permission-boundary.md)
- [Configuration](./configuration.md)
- [API](./api.md)
- [Extension Guide](./extension-guide.md)
- [Samples](./samples.md)

## 참고

필요한 세부 문서는 `architecture.md`, `current-repository-state.md`, `modules.md`, `configuration.md`, `api.md`, `extension-guide.md`, `security.md`를 우선 봅니다.
