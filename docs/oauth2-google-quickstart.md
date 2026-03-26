# Google OAuth2 Quickstart

이 문서는 **현재 구현 기준**으로 `boot-support`를 사용하는 서비스 애플리케이션에서 Google 로그인과 JWT/refresh cookie 발급을 연결하는 최소 예시입니다.

## 전제

- 서비스 애플리케이션은 Spring Boot 3.x
- `io.github.jho951:boot-support:<version>`를 사용
- `spring-boot-starter-security`, `spring-boot-starter-oauth2-client`를 함께 사용
- `OAuth2PrincipalResolver`를 애플리케이션에서 구현

## 1. 의존성

```gradle
dependencies {
    implementation("io.github.jho951:boot-support:<version>")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
}
```

> 현재 저장소에는 별도 `auth-hybrid-spring-boot-starter`가 없습니다. OAuth2 success/failure handler는 `boot-support`가 직접 제공합니다.

## 2. application.yml

```yaml
auth:
  refresh-cookie-name: ADMIN_REFRESH_TOKEN
  refresh-cookie-secure: true
  oauth2:
    enabled: true
  jwt:
    secret: ${AUTH_JWT_SECRET}
    access-seconds: 3600
    refresh-seconds: 1209600

spring:
  security:
    oauth2:
      client:
        registration:
          google:
            client-id: ${GOOGLE_CLIENT_ID}
            client-secret: ${GOOGLE_CLIENT_SECRET}
            scope:
              - openid
              - profile
              - email

```

기본 로그인 시작 URL:

```text
GET /oauth2/authorization/google
```

기본 callback URL:

```text
GET /login/oauth2/code/google
```

## 3. 내부 사용자 매핑

```java
@Component
public class GoogleOAuth2PrincipalResolver implements OAuth2PrincipalResolver {

    private final UserRepository userRepository;

    public GoogleOAuth2PrincipalResolver(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Principal resolve(OAuth2UserIdentity identity) {
        User user = findOrCreateUser(identity);
        return new Principal(String.valueOf(user.getId()), user.getRoles());
    }

    private User findOrCreateUser(OAuth2UserIdentity identity) {
        // providerUserId / email 기준 내부 회원 연결 정책 구현
        throw new UnsupportedOperationException("Implement me");
    }
}
```

## 4. 로그인 성공 응답

설정이 정상이고 `OAuth2PrincipalResolver`가 등록되어 있으면, 로그인 성공 후 기본 응답은 다음 형태입니다.

```http
200 OK
Set-Cookie: ADMIN_REFRESH_TOKEN=...; HttpOnly; Secure; Path=/; SameSite=Lax
Content-Type: application/json

{"accessToken":"..."}
```

## 5. SecurityFilterChain을 직접 가지는 경우

애플리케이션이 자체 `SecurityFilterChain` 빈을 가지면 OAuth2 경로 permit과 success/failure handler 연결을 직접 해줘야 합니다.

```java
@Bean
public SecurityFilterChain filterChain(
    HttpSecurity http,
    AuthOncePerRequestFilter authFilter,
    AuthProperties authProperties,
    @Qualifier("authOAuth2AuthenticationSuccessHandler") AuthenticationSuccessHandler successHandler,
    @Qualifier("authOAuth2AuthenticationFailureHandler") AuthenticationFailureHandler failureHandler
) throws Exception {
    return http
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(auth -> auth
            .requestMatchers(
                authProperties.getOauth2().getAuthorizationBaseUri() + "/**",
                authProperties.getOauth2().getLoginProcessingBaseUri()
            ).permitAll()
            .anyRequest().authenticated()
        )
        .oauth2Login(oauth2 -> oauth2
            .authorizationEndpoint(endpoint ->
                endpoint.baseUri(authProperties.getOauth2().getAuthorizationBaseUri())
            )
            .redirectionEndpoint(endpoint ->
                endpoint.baseUri(authProperties.getOauth2().getLoginProcessingBaseUri())
            )
            .successHandler(successHandler)
            .failureHandler(failureHandler)
        )
        .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class)
        .build();
}
```

## 6. 주의사항

- 현재 기본 success handler는 redirect가 아니라 JSON 응답을 반환합니다.
- Provider별 attribute 구조 해석은 애플리케이션의 `OAuth2PrincipalResolver`가 담당합니다.
- OAuth2 로그인은 인증만 끝내며, 도메인 permission policy는 여전히 애플리케이션 또는 별도 permission 계층이 담당합니다.
