# 모듈 가이드

이 문서는 `auth` 저장소의 모듈이 각각 무엇을 하는지 한눈에 보이게 정리합니다.

## 모듈

- `auth-core`: 외부가 직접 의존하는 공개 모델, SPI, `AuthService`
- `auth-common-test`: 여러 모듈 테스트에서 공통으로 쓰는 픽스처
- `auth-jwt`: JWT를 만들고 검증하는 구현
- `auth-session`: 세션 쿠키와 세션 저장소를 다루는 구현
- `auth-hybrid`: JWT와 세션을 함께 쓰는 조합 로직
- `auth-spring`: Spring 설정 바인딩과 어댑터
- `auth-spring-boot-starter`: 자동 구성과 기본 Bean 제공

## 읽는 법

- API와 SPI를 먼저 이해하려면 `auth-core`부터 보면 됩니다.
- Spring Boot 애플리케이션에서 바로 쓰려면 `auth-spring-boot-starter`가 진입점입니다.
- 세션 또는 OAuth2 연동을 쓰는 경우 `auth-session`과 `auth-hybrid`를 같이 보면 됩니다.

## 배포 대상

- `auth-core`
- `auth-jwt`
- `auth-session`
- `auth-hybrid`
- `auth-spring`
- `auth-spring-boot-starter`
