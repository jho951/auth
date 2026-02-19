# 보안 동작

## 자동 보안 구성

구현:
- `starter/src/main/java/com/auth/config/security/AuthSecurityAutoConfiguration.java`

기본 동작:
- 세션 비활성화(`STATELESS`)
- CSRF/폼로그인/기본로그인/로그아웃 비활성화
- `/auth/**` 허용
- 그 외 요청은 인증 필요
- `AuthOncePerRequestFilter`를 `UsernamePasswordAuthenticationFilter` 앞에 등록

활성 조건:
- Servlet 웹 애플리케이션
- `SecurityFilterChain` 클래스가 classpath에 존재
- `auth.auto-security=true` (기본값)
- 사용자 정의 `SecurityFilterChain` 빈이 없을 때

## 인증 필터

구현:
- `starter/src/main/java/com/auth/config/security/AuthOncePerRequestFilter.java`

처리 단계:
1. `Authorization` 헤더 확인
2. `auth.bearer-prefix` 접두사 검사
3. access token 추출 후 `TokenService.verifyAccessToken` 검증
4. `Principal.roles`를 `SimpleGrantedAuthority`로 변환
5. `SecurityContext`에 인증 객체 저장

예외 발생 시:
- 컨텍스트를 비우고 체인을 계속 진행
- 최종적으로는 `SecurityFilterChain`의 인증 실패/권한 실패 핸들러 응답을 받게 됨

## 기본 예외 응답

`AuthSecurityAutoConfiguration` 기본값:
- 인증 실패(401): `{"message":"UNAUTHORIZED"}`
- 인가 실패(403): `{"message":"FORBIDDEN"}`

## 운영 권장 사항

- 반드시 HTTPS 환경에서 refresh cookie 사용
- `auth.refresh-cookie-secure=true` 유지
- `auth.jwt.secret`은 환경변수/시크릿 매니저로 주입
- refresh 저장소는 운영에서는 메모리 대신 Redis/DB 구현 권장
