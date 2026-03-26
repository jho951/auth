# SPI 확장 가이드

`core`는 구현체를 직접 모르고 `spi` 인터페이스에만 의존합니다.
애플리케이션은 아래 계약을 구현하거나 교체해서 저장소에 맞는 인증 구성을 만들 수 있습니다.

## 인터페이스 위치

- `spi/src/main/java/com/auth/spi/UserFinder.java`
- `spi/src/main/java/com/auth/spi/PasswordVerifier.java`
- `spi/src/main/java/com/auth/spi/TokenService.java`
- `spi/src/main/java/com/auth/spi/RefreshTokenStore.java`
- `spi/src/main/java/com/auth/spi/OAuth2PrincipalResolver.java`

## 필수 구현

### 1) `UserFinder`

역할:

- username으로 인증 대상 사용자 조회

반환 모델:

- `contract/src/main/java/com/auth/api/model/User.java`

예시:

```java
@Component
public class AdminUserFinder implements UserFinder {
    private final UserRepository userRepository;

    public AdminUserFinder(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username)
            .map(e -> new User(
                String.valueOf(e.getId()),
                e.getUsername(),
                e.getPassword(),
                e.getRoles()
            ));
    }
}
```

## 선택 구현 또는 교체

### 2) `PasswordVerifier`

기본값:

- `support`의 `BCryptPasswordVerifier`

언제 교체하나:

- Argon2 등 다른 알고리즘을 쓸 때
- 레거시 해시 포맷과 호환해야 할 때

### 3) `TokenService`

기본값:

- `support`의 `JwtTokenService`

언제 교체하나:

- issuer/audience/jti를 넣고 싶을 때
- asymmetric key, key rotation, external signer를 쓰고 싶을 때
- JWT가 아닌 다른 형식을 쓰고 싶을 때

### 4) `RefreshTokenStore`

기본값:

- `support`의 `InMemoryRefreshTokenStore`

운영 권장:

- Redis 기반 구현
- RDB 기반 구현

관련 문서:

- [redis-refresh-token-store.md](./redis-refresh-token-store.md)

### 5) `OAuth2PrincipalResolver`

역할:

- Provider 사용자 정보를 내부 `Principal`로 매핑

예시:

```java
@Component
public class DefaultOAuth2PrincipalResolver implements OAuth2PrincipalResolver {
    @Override
    public Principal resolve(OAuth2UserIdentity identity) {
        User user = findOrCreateUser(identity);
        return new Principal(user.getUserId(), user.getRoles());
    }
}
```

## `@ConditionalOnMissingBean` 교체 규칙

`boot-support`의 기본 빈들은 대부분 `@ConditionalOnMissingBean`을 사용합니다.
즉, 애플리케이션이 같은 타입의 빈을 먼저 등록하면 기본 구현을 교체할 수 있습니다.

주요 대상:

- `TokenService`
- `PasswordVerifier`
- `RefreshTokenStore`
- `AuthOncePerRequestFilter`
- `RefreshCookieWriter`
- `RefreshTokenExtractor`
- `authOAuth2AuthenticationSuccessHandler`
- `authOAuth2AuthenticationFailureHandler`

## 권장 책임 분리

- Provider 설정, 회원 가입/계정 연결 정책, 리소스 permission policy는 애플리케이션이 소유
- auth 저장소는 principal 생성, 토큰 발급, refresh rotation, access token 검증 필터에 집중
