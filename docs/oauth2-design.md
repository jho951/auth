# OAuth2 설계

현재 저장소에서는 별도 `auth-hybrid-spring-boot-starter`가 아니라 **`boot-support` 안에서 OAuth2 성공 후 처리**를 제공합니다.

## 목표

- Provider별 OAuth2 설정은 서비스 애플리케이션에 남긴다.
- 공통 후처리만 이 저장소에 둔다.
- OAuth2 로그인 성공 후 내부 principal 매핑과 access/refresh 발급을 표준화한다.

## 책임 분리

### 서비스 애플리케이션

- `spring.security.oauth2.client.registration.*` 설정
- Provider 선택과 client id/secret 관리
- 회원 가입 / 계정 연결 정책
- `OAuth2PrincipalResolver` 구현

### auth 저장소

- `OAuth2UserIdentity` 모델 제공
- `OAuth2PrincipalResolver` SPI 제공
- OAuth2 로그인 성공/실패 handler 제공
- 내부 `Principal` 기준 JWT 발급
- refresh cookie 작성 재사용

## 핵심 구성요소

### `OAuth2UserIdentity`

외부 Provider 사용자 정보를 담는 중립 모델입니다.

- `provider`
- `providerUserId`
- `email`
- `name`
- `attributes`

### `OAuth2PrincipalResolver`

애플리케이션이 구현해야 하는 매핑 포트입니다.

```java
public interface OAuth2PrincipalResolver {
    Principal resolve(OAuth2UserIdentity identity);
}
```

### `OAuth2AuthenticationSuccessHandler`

현재 구현이 하는 일:

1. `OAuth2AuthenticationToken`에서 provider 정보와 attribute를 읽음
2. `OAuth2UserIdentity` 생성
3. `OAuth2PrincipalResolver`로 내부 `Principal` 획득
4. `AuthService.login(principal)` 호출
5. JSON body와 refresh cookie를 응답에 기록

### `OAuth2AuthenticationFailureHandler`

현재 구현은 다음 JSON 401 응답을 반환합니다.

```json
{"message":"OAUTH2_AUTHENTICATION_FAILED"}
```

## 자동 구성 조건

`AuthOAuth2AutoConfiguration`은 다음 조건에서 활성화됩니다.

- `org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter`가 classpath에 존재
- `OAuth2PrincipalResolver` bean이 존재
- `auth.oauth2.enabled=true`

## 요청 흐름

1. 클라이언트가 `/oauth2/authorization/{registrationId}` 접근
2. Provider 인증 완료
3. success handler 실행
4. `OAuth2UserIdentity` 생성
5. 내부 `Principal` resolve
6. `AuthService.login(principal)` 호출
7. `{"accessToken":"..."}` + refresh cookie 응답

## 현재 구조상의 한계

- OAuth2 glue가 `boot-support`에 함께 들어 있어 concern 분리가 약함
- 장기적으로는 별도 `auth-hybrid-spring-boot-starter`로 분리하는 것이 더 적합함

이 방향은 [roadmap-target-module-structure.md](./roadmap-target-module-structure.md)에 정리되어 있습니다.
