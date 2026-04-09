# 트러블슈팅

## 1. `AuthService` 빈이 안 만들어진다

### 원인
- `AuthService`는 `UserFinder`가 있어야 생성됩니다.
- 애플리케이션이 `UserFinder`를 등록하지 않았을 가능성이 큽니다.

### 조치
- `UserFinder` 구현을 추가합니다.

## 2. `TokenService` 기본 빈이 안 생긴다

### 원인
- 이미 애플리케이션이 `TokenService` 빈을 직접 등록함

### 조치
- 커스텀 `TokenService` 등록 여부 확인

## 3. `auth.jwt.secret must be at least 32 bytes for HS256`

원인:

- 현재 `JwtTokenService`는 HS256 기준 최소 32바이트 비밀키를 요구합니다.

조치:

- 더 긴 시크릿 사용
- 운영에서는 시크릿 매니저로 주입 권장

## 4. OAuth2 로그인 경로가 404 또는 401이다

원인:

- `spring-boot-starter-oauth2-client` 미포함
- `OAuth2PrincipalResolver` 빈 미등록
- `auth.oauth2.enabled=false`
- `spring.security.oauth2.client.*` 설정 누락

조치:

- OAuth2 Client 의존성 추가
- `OAuth2PrincipalResolver` 구현 추가
- OAuth2 설정과 redirect URI 확인

## 5. refresh cookie가 내려오지 않는다

원인:

- `auth.refresh-cookie-enabled=false`
- refresh cookie를 쓰는 로직이 없음
- 브라우저/프록시가 `Secure` cookie를 차단하는 환경

조치:

- 관련 설정 확인
- HTTPS 환경 여부 확인
- 응답에 `Set-Cookie` 헤더가 있는지 확인

## 6. 로그아웃/재발급이 항상 실패한다

원인:

- refresh token이 서버 저장소에 없음
- 메모리 `RefreshTokenStore`를 여러 인스턴스 환경에서 사용 중

조치:

- 운영에서는 Redis/DB 기반 `RefreshTokenStore` 사용
- refresh rotation 동작과 저장소 key 정책 확인

## 7. Gradle property가 안 읽힌다

원인:

- 현재 아카이브에 `grade.properties`라는 파일명이 존재할 수 있음
- Gradle 일반 규약 파일명은 `gradle.properties`

조치:

- 파일명을 확인하고 필요하면 `gradle.properties`로 정리

## 8. artifact 이름이 문서와 다르다

원인:

- 현재 퍼블리싱은 `artifactId = project.name`을 사용

조치:

- 문서가 current implementation 문맥인지 먼저 확인
- 현재 좌표는 [current-repository-state.md](./current-repository-state.md)와 [modules.md](./modules.md) 참고
