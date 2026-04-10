# 모듈 가이드

이 문서는 `auth` 저장소의 모듈이 각각 무엇을 하는지 한눈에 보이게 정리합니다.

## 모듈

- `auth-core`: 공통 모델, 인증 연동 포인트, `AuthService`
- `auth-jwt`: JWT를 만들고 검증하는 구현
- `auth-session`: 세션 저장소와 세션 인증 로직을 다루는 구현
- `auth-hybrid`: JWT와 세션을 함께 쓰는 조합 로직

## 읽는 법

- 공통 모델과 연동 포인트를 먼저 이해하려면 `auth-core`부터 보면 됩니다.
- 세션 저장, principal 매핑, JWT와 세션 조합이 필요하면 `auth-session`과 `auth-hybrid`를 같이 보면 됩니다.

## 배포 대상

- `auth-core`
- `auth-jwt`
- `auth-session`
- `auth-hybrid`
