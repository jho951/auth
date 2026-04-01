# RefreshCookieWriter 상세

## 위치

- 구현 파일: `auth-spring-boot-starter/src/main/java/com/auth/config/controller/RefreshCookieWriter.java`
- 테스트 파일: `auth-spring-boot-starter/src/test/java/com/auth/config/controller/RefreshCookieWriterTest.java`

## 역할

`RefreshCookieWriter`는 `Tokens`와 기본 `ResponseEntity`를 받아 **응답 body를 유지한 채 refresh cookie만 추가하거나 제거**하는 웹 보조 클래스입니다.

## 생성자 의존성

- `AuthProperties`
- refresh token 수명 값

즉, 다음 정보를 사용합니다.

- cookie 이름
- HttpOnly / Secure / Path / SameSite
- refresh cookie `Max-Age`

## 주요 메서드

### `write(Tokens tokens, ResponseEntity<T> base)`

동작:

- `auth.refresh-cookie-enabled=false`면 원본 응답을 그대로 반환
- 그렇지 않으면 `Set-Cookie` 헤더를 추가한 새 `ResponseEntity` 반환

### `clear(ResponseEntity<Void> base)`

동작:

- `Max-Age=0`인 cookie를 내려 기존 refresh cookie를 삭제

## 사용 예시

```java
@PostMapping("/login")
public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequest request) {
    Tokens tokens = authService.login(request.username(), request.password());
    ResponseEntity<Map<String, String>> base = ResponseEntity.ok(Map.of(
        "accessToken", tokens.getAccessToken()
    ));
    return refreshCookieWriter.write(tokens, base);
}
```

로그아웃 시 cookie 제거 예시:

```java
@PostMapping("/logout")
public ResponseEntity<Void> logout(HttpServletRequest request) {
    String refreshToken = refreshTokenExtractor.extract(request);
    authService.logout(refreshToken);
    return refreshCookieWriter.clear(ResponseEntity.noContent().build());
}
```

## 주의사항

- 현재 기본 구현은 refresh token을 **body가 아니라 cookie**로 주고 싶을 때만 도와줍니다.
- access token을 cookie로 내리는 기능은 포함하지 않습니다.
- 이 클래스는 `auth-spring-boot-starter`에 있습니다.
