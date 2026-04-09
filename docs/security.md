# 보안 동작

이 문서는 이 저장소의 보안 관련 구현이 어떤 책임을 가지는지 설명합니다.

## 기본 원칙

- 이 저장소는 인증 결과를 만드는 도구를 제공합니다.
- 애플리케이션의 최종 인가 정책은 애플리케이션이 책임집니다.
- Spring Security 구성은 애플리케이션이 직접 조립합니다.

## `auth-session`

구현:

- `auth-session/src/main/java/com/auth/session/security/SessionAuthenticationFilter.java`

역할:

- session cookie를 읽습니다.
- `SessionAuthenticationProvider`를 호출합니다.
- `Principal`을 `SecurityContext`에 넣습니다.
- 실패하면 `SecurityContextHolder`를 비웁니다.

## `auth-jwt`

구현:

- `auth-jwt/src/main/java/com/auth/support/jwt/JwtTokenService.java`

역할:

- access token과 refresh token의 발급/검증을 담당합니다.
- `TokenService` 구현체로 사용됩니다.

## 운영 권장 사항

- JWT 비밀키는 환경변수나 시크릿 저장소로 주입합니다.
- 세션 저장소는 여러 인스턴스 환경에서 메모리 구현 대신 외부 저장소를 사용합니다.
- 쿠키를 쓸 때는 HTTPS 환경과 `HttpOnly`/`SameSite` 설정을 함께 확인합니다.
