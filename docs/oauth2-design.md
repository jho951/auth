# OAuth2 설계

현재 저장소는 `auth-spring-boot-starter`를 통해 OAuth2 성공 후 처리와 refresh cookie 작성을 제공합니다.

## 목표

- Provider별 OAuth2 설정은 서비스 애플리케이션에 남긴다.
- 공통 후처리만 이 저장소에 둔다.
- OAuth2 로그인 성공 후 내부 principal 매핑과 access/refresh 발급을 표준화한다.
- 샘플은 기본값에서 OAuth2를 비활성화한 상태로 실행 가능해야 한다.

## 책임 분리

### 서비스 애플리케이션

- `spring.security.oauth2.client.registration.*` 설정
- Provider 선택과 client id/secret 관리
- 회원 가입 / 계정 연결 정책
- `OAuth2PrincipalResolver` 구현

### OSS auth 저장소

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

## 최소 사용 예시

```gradle
dependencies {
    implementation("io.github.jho951:auth-core:<version>")
    implementation("io.github.jho951:auth-spring:<version>")
    implementation("io.github.jho951:auth-spring-boot-starter:<version>")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
}
```

```java
@Component
public class MyOAuth2PrincipalResolver implements OAuth2PrincipalResolver {
    @Override
    public Principal resolve(OAuth2UserIdentity identity) {
        return new Principal(identity.getProviderUserId(), java.util.List.of());
    }
}
```

이 문서는 provider별 상세 예제가 아니라, OAuth2 연결의 최소 wiring만 설명합니다.
