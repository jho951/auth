# 로드맵: 목표 모듈 구조

> 이 문서는 **현재 구현 구조가 아니라 목표 구조**를 설명합니다.

## 목표

현재 layered 구조를 다음과 같은 feature-oriented 구조로 리팩터링하는 것이 목표입니다.

- `auth-core`
- `auth-common-test`
- `auth-jwt`
- `auth-session`
- `auth-hybrid`
- `auth-spring`
- `auth-jwt-spring-boot-starter`
- `auth-session-spring-boot-starter`
- `auth-hybrid-spring-boot-starter`
- `samples/*`

## 왜 바꾸려는가

현재 구조에서는 다음 concern이 섞여 있습니다.

- 공개 계약(`contract`)
- 도메인 로직(`core`)
- 기본 구현체(`support`)
- Spring 연동과 자동 구성(`boot-support`)

이 구조는 작은 저장소에서는 단순하지만, 다음 요구가 늘수록 분리가 필요합니다.

- JWT 전용 사용
- 세션 전용 사용
- OAuth2 + JWT 하이브리드 사용
- Spring 의존 없이 core만 재사용
- 테스트 픽스처 재사용

## 목표 역할 분리

### `auth-core`

- 모델, 예외, SPI, 순수 인증 유즈케이스

### `auth-jwt`

- JWT 전용 `TokenService` 구현

### `auth-session`

- 세션 인증용 저장소/매퍼/추출기/인증 공급자

### `auth-hybrid`

- 세션과 JWT 조합 전략

### `auth-spring`

- Spring Security 브릿지, 필터 추상화, argument resolver 같은 공통 연동부

### `*-starter`

- 각 방식별 Spring Boot 자동 구성

## 현재와의 관계

- 현재: `boot-support`가 Spring 관련 concern을 거의 모두 갖고 있음
- 목표: Spring 브릿지와 방식별 starter를 분리

- 현재: `support` 안에 JWT, password, refresh store 구현이 섞여 있음
- 목표: JWT는 `auth-jwt`, 나머지는 적절히 분리

## 상태

- 설계 방향은 정리되었음
- 현재 저장소에는 아직 이 구조가 적용되지 않음
- 실제 매핑 계획은 [migration-guide.md](./migration-guide.md)에 정리
