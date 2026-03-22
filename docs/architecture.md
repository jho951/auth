# 아키텍처 개요

## 설계 목표

- 인증 도메인 로직을 프레임워크/DB 구현에서 분리
- 모듈화로 재사용성과 교체 가능성 확보
- Spring Boot 환경에서는 자동 설정으로 빠른 적용

## 레이어 구조

- `contract`: 외부에 노출되는 모델/예외/에러코드
- `spi`: 코어가 의존하는 포트(인터페이스)
- `core`: 인증 유즈케이스(`AuthService`)
- `support`: 순수 Java 기반 기본 구현체
- `boot-support`: Spring Boot 자동 설정, Security 필터, 쿠키 유틸, 지원 모듈 조립
- `common`: 공용 유틸

설계 원칙:
- `core`, `spi`, `contract`, `support` 는 Spring 없이도 사용할 수 있어야 합니다.
- `boot-support` 는 Spring Boot 환경에서 위 구성요소를 연결하는 어댑터 역할만 담당합니다.

## 요청 처리 흐름

## 1) 로그인

1. 서비스 애플리케이션의 로그인 컨트롤러가 요청을 받음
2. 컨트롤러가 `AuthService.login` 실행
3. `AuthService`가 `UserFinder`, `PasswordVerifier`로 인증
4. `TokenService`로 access/refresh 발급
5. `RefreshTokenStore`에 refresh 저장
6. 필요하면 `RefreshCookieWriter`가 refresh 쿠키를 `Set-Cookie`로 작성
7. 응답 바디 구조는 서비스 애플리케이션이 결정

관련 코드:
- `core/src/main/java/com/auth/core/service/AuthService.java`
- `support/src/main/java/com/auth/support/jwt/JwtTokenService.java`
- `support/src/main/java/com/auth/support/refresh/memory/InMemoryRefreshTokenStore.java`

### OAuth 로그인 확장

OAuth2/OIDC 로그인에서는 Provider 인증 자체를 서비스 애플리케이션이 담당합니다.

1. 서비스 애플리케이션이 Google/GitHub/Kakao 인증 완료
2. Provider 사용자 정보를 내부 사용자로 매핑
3. 내부 사용자 ID/권한으로 `Principal` 생성
4. `AuthService.login(Principal)` 호출
5. 이후 access/refresh 발급 및 refresh 저장 흐름은 일반 로그인과 동일

## 2) 토큰 재발급 (Refresh Rotation)

1. 서비스 애플리케이션의 재발급 컨트롤러가 요청을 받음
2. 필요하면 `RefreshTokenExtractor`로 refresh cookie 추출
3. `AuthService.refresh`가 refresh 토큰 서명/타입 검증
4. 저장소 존재 여부 확인(`exists`)
5. 기존 refresh 폐기(`revoke`)
6. 새 access/refresh 발급 후 새 refresh 저장(`save`)
7. 필요하면 새 refresh를 쿠키로 갱신

## 3) 로그아웃

1. 서비스 애플리케이션의 로그아웃 컨트롤러가 요청을 받음
2. 필요하면 `RefreshTokenExtractor`로 refresh cookie 추출
3. `AuthService.logout`이 저장소에서 refresh 폐기
4. 필요하면 `RefreshCookieWriter.clear`가 만료 쿠키(`Max-Age=0`) 전송

## 인증 필터 흐름

1. `AuthOncePerRequestFilter`가 `Authorization` 헤더 확인
2. `auth.bearer-prefix`로 시작하면 access token 파싱
3. `TokenService.verifyAccessToken`으로 `Principal` 복원
4. SecurityContext에 인증 객체 저장

관련 코드:
- `boot-support/src/main/java/com/auth/config/security/AuthOncePerRequestFilter.java`

## 자동 구성 진입점

Spring Boot 자동 구성 등록 파일:
- `boot-support/src/main/resources/META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports`

등록 클래스:
- `com.auth.config.AuthAutoConfiguration`
- `com.auth.config.security.AuthSecurityAutoConfiguration`
- `com.auth.config.oauth.AuthOAuth2AutoConfiguration`
