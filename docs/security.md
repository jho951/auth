# 보안 동작

이 문서는 `auth-spring-boot-starter`가 Spring Security와 어떻게 연결되는지 설명합니다.

## 기본 SecurityFilterChain

구현:

- `auth-spring-boot-starter/src/main/java/com/auth/config/security/AuthSecurityAutoConfiguration.java`

기본 동작:

- stateless session policy (`SessionCreationPolicy.STATELESS`)
- csrf / formLogin / httpBasic / logout 비활성화
- OAuth2 시작/콜백 경로 permitAll
- 그 외 요청은 authenticated
- `AuthOncePerRequestFilter`를 `UsernamePasswordAuthenticationFilter` 앞에 등록

쉽게 말하면:

- 인증 헤더가 있으면 먼저 검사합니다.
- 토큰이 유효하면 `Principal`을 `SecurityContext`에 넣습니다.
- 토큰이 없거나 잘못되면 요청은 계속 흘려보내고, 최종 응답은 Spring Security가 결정합니다.

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

### 기본값

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

이 저장소는 인증 결과를 `Principal`과 `GrantedAuthority` 형태로 전달하지만, 리소스 접근 허용/거부 정책 자체는 애플리케이션이 책임집니다.

- 이 저장소: 인증과 principal 전달
- 애플리케이션/permission 계층: 리소스 접근 허용/거부 판단

## 운영 권장 사항

- `auth.jwt.secret`은 환경변수나 시크릿 저장소로 주입
- refresh cookie는 HTTPS 환경에서 사용
- 기본 메모리 구현은 `auth-spring-boot-starter`가 제공하므로, 운영에서는 이를 Redis/DB 구현으로 교체
- 자체 `SecurityFilterChain`을 쓰는 경우 permit 경로와 handler 연결을 명시적으로 구성
