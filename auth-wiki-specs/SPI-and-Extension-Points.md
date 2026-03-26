# SPI and Extension Points

## 목적

이 문서는 `auth` 모듈에서 서비스 애플리케이션이 반드시 구현하거나,
확장 가능하도록 열어두는 인터페이스를 정리합니다.

---

## 현재 존재하는 SPI

### `UserFinder`

역할:
- 로그인 식별자(username/email 등)로 인증 대상 사용자를 조회

계약:
- 유저가 없으면 `Optional.empty()`
- 반환 타입은 auth 모듈의 최소 `User` 모델

애플리케이션 책임:
- DB/Repository 연동
- 상태값(활성/비활성/잠금) 반영 여부

---

### `PasswordVerifier`

역할:
- 사용자가 입력한 평문 비밀번호와 저장된 해시 비교

현재 기본 구현:
- `BCryptPasswordVerifier`

애플리케이션 책임:
- 해시 저장 정책
- 해시 업그레이드 정책

---

### `TokenService`

역할:
- access/refresh token 발급 및 검증

현재 기본 구현:
- `JwtTokenService`

계약:
- `issueAccessToken(Principal)`
- `issueRefreshToken(Principal)`
- `verifyAccessToken(String)`
- `verifyRefreshToken(String)`

---

### `RefreshTokenStore`

역할:
- 서버 기준 refresh token 상태 저장/조회/폐기

현재 기본 구현:
- `InMemoryRefreshTokenStore`

계약:
- `save(userId, refreshToken, expiresAt)`
- `exists(userId, refreshToken)`
- `revoke(userId, refreshToken)`

---

### `OAuth2PrincipalResolver`

역할:
- 외부 OAuth2/OIDC Provider 사용자 정보를 내부 `Principal`로 변환

입력:
- `OAuth2UserIdentity`

출력:
- `Principal`

애플리케이션 책임:
- 사용자 연결/가입/계정 생성 여부 판단
- provider attribute 해석

---

## 현재 SPI 설계 원칙

- 코어는 구현이 아닌 인터페이스에 의존한다.
- 서비스마다 달라지는 저장소/계정 모델은 SPI로 분리한다.
- auth 모듈은 “최소 인증 모델”만 안다.
- HTTP 응답 포맷과 API shape는 애플리케이션이 결정한다.

---

## 목표 구조에서 추가될 수 있는 SPI

### Session 계열
- `SessionStore`
- `SessionPrincipalMapper`

### Hybrid 계열
- `AuthenticationStrategyResolver`
- `CompositeAuthenticationProvider`

### JWT 계열
- `ClaimValidator`
- `JwtKeyResolver`
- `PrincipalMapper`

---

## SPI 설계 규칙

1. SPI는 도메인 최소 모델만 주고받는다.
2. Spring 타입을 SPI 시그니처에 직접 노출하지 않는다.
3. 애플리케이션 책임과 라이브러리 책임을 섞지 않는다.
4. SPI는 구현체보다 오래 살아야 한다.
5. 기본 구현이 있더라도 인터페이스 계약이 먼저 문서화되어야 한다.

---

## 구현 예시: `UserFinder`

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

---

## 위키 운영 규칙

- 새로운 SPI를 추가하면 반드시 이 페이지를 함께 업데이트한다.
- SPI가 목표 구조용 명세인지, 현재 구현된 인터페이스인지 구분해서 적는다.
- starter 전용 설정 포인트는 SPI가 아니라 configuration 문서에 기록한다.
