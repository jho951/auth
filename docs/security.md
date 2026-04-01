# 보안 동작

이 문서는 **현재 `auth-spring-boot-starter` 기준** Spring Security 연동 동작을 설명합니다.

## 기본 SecurityFilterChain

구현:

- `auth-spring-boot-starter/src/main/java/com/auth/config/security/AuthSecurityAutoConfiguration.java`

기본 동작:

- stateless session policy (`SessionCreationPolicy.STATELESS`)
- csrf / formLogin / httpBasic / logout 비활성화
- OAuth2 시작/콜백 경로 permitAll
- 그 외 요청은 authenticated
- `AuthOncePerRequestFilter`를 `UsernamePasswordAuthenticationFilter` 앞에 등록

활성 조건:

- Servlet 웹 애플리케이션
- `SecurityFilterChain` classpath 존재
- `auth.auto-security=true` (기본값)
- 애플리케이션이 자체 `SecurityFilterChain` 빈을 제공하지 않음

## `AuthOncePerRequestFilter`

구현:

- `auth-spring-boot-starter/src/main/java/com/auth/config/security/AuthOncePerRequestFilter.java`

처리 단계:

1. `Authorization` 헤더 확인
2. `auth.bearer-prefix` 접두사 검사
3. access token 추출
4. `TokenService.verifyAccessToken`으로 `Principal` 복원
5. `Principal`의 authorities/attributes를 `SimpleGrantedAuthority`로 변환
6. `SecurityContextHolder`에 인증 정보 저장

예외 발생 시:

- `SecurityContextHolder`를 비움
- 필터 체인을 계속 진행
- 최종 401/403 응답은 `SecurityFilterChain`의 예외 처리기가 결정

## 기본 예외 응답

기본값:

- 401: `{"message":"UNAUTHORIZED"}`
- 403: `{"message":"FORBIDDEN"}`

## OAuth2 경로 허용

기본 permit 경로:

- `auth.oauth2.authorization-base-uri + "/**"`
- `auth.oauth2.login-processing-base-uri`

즉 기본값이면 아래 경로가 허용됩니다.

- `/oauth2/authorization/**`
- `/login/oauth2/code/*`

## `SessionAuthenticationFilter`

구현:

- `auth-session/src/main/java/com/auth/session/security/SessionAuthenticationFilter.java`

처리 단계:

1. session cookie 추출
2. `SessionAuthenticationProvider.authenticate` 호출
3. `Principal`을 `SecurityContext`에 저장
4. 실패 시 `SecurityContextHolder`를 비움

## auth와 authorization 경계

현재 필터가 `roles` 또는 authorities를 `GrantedAuthority`로 바꾸더라도, 이 저장소가 리소스 authorization policy를 책임지는 것은 아닙니다.

- 이 저장소: 인증과 principal 전달
- 애플리케이션/permission 계층: 리소스 접근 허용/거부 판단

자세한 기준은 [auth-vs-permission-boundary.md](./auth-vs-permission-boundary.md)를 참고합니다.

## 운영 권장 사항

- `auth.jwt.secret`은 환경변수나 시크릿 저장소로 주입
- refresh cookie는 HTTPS 환경에서 사용
- 운영에서는 `InMemoryRefreshTokenStore` 대신 Redis/DB 구현 사용
- 자체 `SecurityFilterChain`을 쓰는 경우 permit 경로와 handler 연결을 명시적으로 구성
