# Samples

## 목적

`samples` 디렉터리는 문서와 실제 사용 예제를 연결하는 역할을 합니다.

이 프로젝트에서 샘플은 단순 데모가 아니라 다음 목적을 갖습니다.

- starter 사용법의 실행 가능한 예시
- 문서 검증용 기준 애플리케이션
- 통합 테스트 대상
- OSS 사용자가 가장 먼저 참고하는 진입점

---

## 목표 구조

```text
samples
├─ sample-jwt-api
├─ sample-session-web
└─ sample-hybrid-sso
```

---

## sample-jwt-api

### 목적
- 순수 JWT 기반 API 서버 예제
- `auth-jwt-spring-boot-starter` 사용 예시
- Bearer token 인증 흐름 설명

### 포함 내용
- 로그인 API
- refresh API
- 로그아웃 API
- 보호된 API
- `UserFinder` 예제 구현

### 보여줘야 하는 것
- `auth.jwt.*` 설정
- `AuthService` 사용 방식
- `Authorization: Bearer` 요청 예시

---

## sample-session-web

### 목적
- 세션 기반 웹 애플리케이션 예제
- `auth-session-spring-boot-starter` 사용 예시
- 세션 cookie 기반 현재 사용자 복원 예시

### 포함 내용
- 로그인 후 세션 생성
- 세션 기반 보호 페이지
- 로그아웃 처리
- 현재 사용자 주입 예시

### 보여줘야 하는 것
- 세션 저장소 설정
- session cookie 전략
- JWT 없이도 동작하는 구조

---

## sample-hybrid-sso

### 목적
- OAuth2 로그인 + 내부 JWT 발급 + refresh cookie 흐름 예제
- `auth-hybrid-spring-boot-starter` 사용 예시

### 포함 내용
- OAuth2 login
- `OAuth2PrincipalResolver` 예제 구현
- access token JSON 응답
- refresh cookie 작성
- 보호된 API 호출

### 보여줘야 하는 것
- `spring.security.oauth2.client.*` 설정
- `auth.oauth2.*` 설정
- `RefreshCookieWriter` 동작

---

## 샘플 운영 원칙

- README 만으로 실행 가능해야 한다.
- 위키 문서의 설정 예시와 동기화되어야 한다.
- 최소 하나 이상의 통합 테스트를 가져야 한다.
- 각 샘플은 정확히 한 starter 를 대표해야 한다.
