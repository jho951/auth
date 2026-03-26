# Architecture

## 설계 목표

이 저장소의 아키텍처는 다음 목표를 만족해야 합니다.

- 인증 유즈케이스를 프레임워크/웹 계층에서 분리한다.
- JWT only / Session only / Hybrid 프로젝트를 모두 지원한다.
- Spring Boot를 쓰지 않는 환경에서도 코어와 기본 구현체를 재사용할 수 있게 한다.
- Boot starter는 조립 계층만 담당하고, 인증 메커니즘 자체는 독립 모듈로 유지한다.
- 서비스 애플리케이션은 인증 방식 대신 **최종 인증 결과**만 소비하도록 만든다.

---

## 책임 경계

### auth 모듈이 책임지는 것

- 사용자 인증 결과를 표현하는 공통 모델
- 로그인/재발급/로그아웃 유즈케이스
- 토큰 발급/검증
- 리프레시 토큰 회전 정책
- Spring Security 연동
- OAuth2 로그인 이후 내부 Principal 매핑과 JWT 발급 연결
- 테스트/샘플 애플리케이션 지원

### auth 모듈이 책임지지 않는 것

- 회원가입/탈퇴
- 비밀번호 재설정 화면
- 소셜 로그인 Provider 등록 화면
- 메뉴 권한/리소스 권한의 최종 판단
- 사용자/조직/테넌트 관리 UI
- 각 서비스별 비즈니스 인가 정책

---

## 목표 멀티모듈 구조

```text
auth
├─ auth-core
├─ auth-common-test
├─ auth-jwt
├─ auth-session
├─ auth-hybrid
├─ auth-spring
├─ auth-jwt-spring-boot-starter
├─ auth-session-spring-boot-starter
├─ auth-hybrid-spring-boot-starter
├─ samples
│  ├─ sample-jwt-api
│  ├─ sample-session-web
│  └─ sample-hybrid-sso
└─ docs
```

---

## 의존 방향

```text
auth-core
   ↑
auth-jwt        auth-session
   ↑               ↑
   └──────┬────────┘
          ↑
      auth-hybrid

auth-core ──→ auth-spring

auth-jwt + auth-spring ──→ auth-jwt-spring-boot-starter
auth-session + auth-spring ──→ auth-session-spring-boot-starter
auth-hybrid + auth-spring ──→ auth-hybrid-spring-boot-starter
```

원칙은 다음과 같습니다.

- `auth-core`는 가장 아래에 위치한다.
- `auth-core`는 Spring, JWT 라이브러리, HttpSession에 의존하지 않는다.
- `auth-jwt`, `auth-session`, `auth-hybrid`는 구현 전략 모듈이다.
- `auth-spring`은 Spring Security/Web 연동 계층이다.
- `*-starter`는 오직 조립과 자동 설정만 담당한다.

---

## 요청 흐름

### 1. 일반 로그인

1. 서비스 애플리케이션이 로그인 요청을 받는다.
2. 애플리케이션이 `AuthService.login(username, password)`를 호출한다.
3. `UserFinder`로 사용자 조회.
4. `PasswordVerifier`로 비밀번호 확인.
5. `TokenService`로 access/refresh 발급.
6. `RefreshTokenStore`에 refresh 저장.
7. 웹 애플리케이션이면 `RefreshCookieWriter`로 refresh cookie를 작성할 수 있다.

### 2. Refresh Rotation

1. 애플리케이션이 refresh token을 읽는다.
2. `AuthService.refresh(refreshToken)` 호출.
3. `TokenService.verifyRefreshToken`으로 토큰 서명/만료/타입 검증.
4. `RefreshTokenStore.exists`로 서버 저장소 기준 유효성 확인.
5. 이전 refresh revoke.
6. 새 access/refresh 발급.
7. 새 refresh 저장.

### 3. JWT 기반 요청 인증

1. `Authorization: Bearer <token>` 헤더 수신.
2. `AuthOncePerRequestFilter` 또는 향후 JWT 전용 filter가 access token 추출.
3. `TokenService.verifyAccessToken` 수행.
4. `Principal` 또는 향후 `AuthPrincipal`로 복원.
5. Spring Security `SecurityContext`에 인증 객체 저장.

### 4. OAuth2 / Hybrid 로그인

1. OAuth2 Provider 인증은 Spring Security OAuth2 Client가 담당.
2. 성공 시 `OAuth2PrincipalResolver`가 외부 사용자 정보를 내부 Principal로 변환.
3. `AuthService.login(principal)`이 JWT 발급.
4. 필요하면 access token은 JSON body, refresh token은 cookie로 전달.
5. 이후 API 호출은 JWT 또는 세션 기반으로 처리.

---

## 현재 저장소에서 확인된 구조적 특징

- `AuthService`는 순수 Java 유즈케이스로 잘 분리되어 있다.
- `JwtTokenService`는 HS256 기반 기본 구현체로 동작한다.
- `boot-support`가 자동 설정, 필터, 보안 체인, OAuth2 연결, refresh cookie 처리까지 모두 맡고 있어 책임이 넓다.
- `auth-session`, `auth-hybrid`, `auth-common-test`에 해당하는 독립 모듈은 아직 없다.

---

## 아키텍처 결론

현재 저장소는 “인증 도메인 + JWT 지원 + Spring Boot 조립”까지는 이미 성숙한 구조를 갖고 있다.  
다만 **지원 대상이 늘어날수록 `boot-support`와 `support`의 책임이 비대해지므로**, 목표 구조에서는 다음이 핵심이다.

- 코어 통합
- 전략별 구현 분리
- Spring bridge 분리
- starter 조립 분리
- spec-first module 도입
