# RefreshCookieWriter 가이드

`RefreshCookieWriter`는 로그인/토큰 재발급 응답에 Refresh Token 쿠키를 붙이거나, 로그아웃 시 쿠키를 제거하는 역할을 담당합니다.

- 구현 파일: `starter/src/main/java/com/auth/config/controller/RefreshCookieWriter.java`
- 테스트 파일: `starter/src/test/java/com/auth/config/controller/RefreshCookieWriterTest.java`

## 왜 필요한가

API 응답 바디에는 Access Token만 내려주고, Refresh Token은 HttpOnly 쿠키로 관리하면 브라우저 환경에서 보안적으로 더 안전하게 다룰 수 있습니다.

## 주요 동작

## 1) `write(Tokens, ResponseEntity<LoginResponse>)`

로그인/재발급 성공 시 호출됩니다.

- `auth.refresh-cookie-enabled=false` 이면 원래 응답을 그대로 반환합니다.
- `true`이면 `Set-Cookie` 헤더를 생성합니다.

쿠키 설정 값은 다음 프로퍼티에서 가져옵니다.

- `auth.refresh-cookie-name`
- `auth.refresh-cookie-http-only`
- `auth.refresh-cookie-secure`
- `auth.refresh-cookie-path`
- `auth.refresh-cookie-same-site`
- `auth.jwt.refresh-seconds` (Max-Age)

반환되는 응답은 다음을 유지합니다.

- 기존 HTTP status
- 기존 body (`LoginResponse`)

## 2) `clear(ResponseEntity<Void>)`

로그아웃 시 호출됩니다.

- `auth.refresh-cookie-enabled=false` 이면 원래 응답을 그대로 반환합니다.
- `true`이면 같은 쿠키 이름/경로로 `Max-Age=0` 쿠키를 내려 브라우저에서 삭제되도록 합니다.

## 사용 흐름

`AuthController`에서 아래처럼 사용합니다.

1. 로그인 성공 후 `refreshCookieWriter.write(...)`
2. 로그아웃 시 `refreshCookieWriter.clear(...)`

관련 위치: `starter/src/main/java/com/auth/config/controller/AuthController.java`

## 테스트 설명

`RefreshCookieWriterTest`는 아래 시나리오를 검증합니다.

1. 쿠키 비활성 시 `write`가 기존 응답 객체를 그대로 반환하는지
2. 쿠키 활성 시 `write`가 `Set-Cookie`를 추가하고 status/body를 유지하는지
3. 쿠키 활성 시 `clear`가 `Max-Age=0` 만료 쿠키를 내려주는지
4. 쿠키 비활성 시 `clear`가 기존 응답 객체를 그대로 반환하는지

## 테스트 실행

```bash
./gradlew :starter:test --tests "com.auth.config.controller.RefreshCookieWriterTest"
```

## 참고

사용자 요청에 `refreeshCookieWriter`로 표기되었더라도 실제 클래스명은 `RefreshCookieWriter`입니다.
