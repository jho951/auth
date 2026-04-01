# 샘플 사용법

이 문서는 저장소 안의 최소 샘플 3개를 어떻게 실행하고 무엇을 확인하면 되는지 정리합니다.

## 공통 전제

- 모든 샘플은 `auth-spring-boot-starter`를 사용합니다.
- `sample-jwt-api`와 `sample-session-web`은 애플리케이션 안에 `UserFinder`와 `PasswordVerifier`를 이미 구현해 둔 상태입니다.
- `sample-hybrid-sso`는 OAuth2 principal resolver만 포함하고, OAuth2 토큰 발급은 starter가 직접 처리합니다.
- 실제 서비스에서는 각 샘플의 구현을 애플리케이션 데이터 소스와 외부 인증 설정에 맞게 교체하면 됩니다.

## `sample-jwt-api`

포트:

- `8081`

기본 계정:

- `admin / admin`
- `user / user`

제공 엔드포인트:

- `GET /ping`
- `POST /api/login`
- `GET /api/profile`

설정 포인트:

- refresh cookie 이름: `JWT_REFRESH_TOKEN`
- access token TTL: `600`초
- refresh token TTL: `1209600`초

실행 확인 순서:

1. 애플리케이션을 실행한다.
2. `POST /api/login`에 `admin` 또는 `user` 계정으로 로그인한다.
3. 응답 body의 `accessToken`과 `Set-Cookie` 헤더를 확인한다.
4. `Authorization: Bearer <accessToken>`으로 `/api/profile`을 호출한다.

curl 예시:

```bash
curl -i http://localhost:8081/ping

curl -i -X POST http://localhost:8081/api/login \
  -H 'Content-Type: application/json' \
  -d '{"username":"admin","password":"admin"}'

curl -i http://localhost:8081/api/profile \
  -H 'Authorization: Bearer <accessToken>'
```

## `sample-session-web`

포트:

- `8082`

기본 계정:

- `alice / alice`
- `admin / admin`

제공 엔드포인트:

- `GET /session-ping`
- `POST /session/login`
- `GET /session/me`
- `POST /session/logout`

설정 포인트:

- session cookie 이름: `AUTH_SESSION`
- session TTL: `PT30M`

실행 확인 순서:

1. 애플리케이션을 실행한다.
2. `POST /session/login`에 `alice` 또는 `admin` 계정으로 로그인한다.
3. 응답의 session cookie를 확인한다.
4. 그 cookie를 사용해 `/session/me`를 호출한다.
5. `/session/logout`으로 세션을 폐기한다.

curl 예시:

```bash
curl -i http://localhost:8082/session-ping

curl -i -c /tmp/auth-session-cookie.txt -X POST http://localhost:8082/session/login \
  -H 'Content-Type: application/json' \
  -d '{"username":"alice","password":"alice"}'

curl -i -b /tmp/auth-session-cookie.txt http://localhost:8082/session/me

curl -i -b /tmp/auth-session-cookie.txt -X POST http://localhost:8082/session/logout
```

## `sample-hybrid-sso`

포트:

- `8083`

기본 상태:

- `auth.oauth2.enabled=false`
- `spring.security.oauth2.client.*` 설정 없음
- 따라서 기본 실행만 확인할 수 있고, 실제 OAuth2 로그인은 비활성 상태다.
- 샘플 내부에는 `OAuth2PrincipalResolver`만 포함되어 있어서, hybrid wiring 자체를 최소 구성으로 보여준다.

제공 엔드포인트:

- `GET /hybrid-ping`
- `GET /hybrid/me`

실행 확인 순서:

1. 애플리케이션을 실행한다.
2. `/hybrid-ping`으로 기본 기동을 확인한다.
3. OAuth2 로그인을 사용하려면 `auth.oauth2.enabled=true`와 `spring.security.oauth2.client.registration.*`, `spring.security.oauth2.client.provider.*`를 실제 값으로 넣는다.
4. `OAuth2PrincipalResolver` 구현체는 샘플에 이미 포함되어 있으므로, Provider 설정만 추가하면 된다.

curl 예시:

```bash
curl -i http://localhost:8083/hybrid-ping

curl -i http://localhost:8083/oauth2/authorization/google
```

`/oauth2/authorization/google`는 `auth.oauth2.enabled=true`와 실제 `spring.security.oauth2.client.*` 설정이 있을 때만 동작합니다. OAuth2 로그인 완료 후에는 브라우저에서 리다이렉트된 상태로 `/hybrid/me`를 확인하세요.

## 참고

- 샘플은 최소 연결 예시이므로 운영용 보안 정책을 그대로 보여주지 않는다.
- 실제 서비스에서는 샘플의 in-memory 저장소와 평문 비밀번호 verifier를 교체해야 한다.
