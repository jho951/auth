# Auth Module
### 재사용 가능한 인증 라이브러리입니다.

## 🚀 목표

- 인증 로직을 애플리케이션 코드에서 분리
- JWT 기반 토큰 인증의 표준화
- 서비스별 사용자 저장소(DB) 차이를 SPI로 분리
- 설정(application.yml)만으로 빠른 적용

---

## 🧱 프로젝트 구조
``` text
├─ contract/
├─ core/
├─ spi/
├─ starter/
├─ docs/
├─ gradle/
├─ build.gradle
├─ gradle.properties
└─ settings.gradle
```
---

## 📚 문서

- 문서 진입점: [docs/README.md](./docs/README.md)
- 아키텍처 개요: [docs/architecture.md](./docs/architecture.md)
- 모듈 가이드: [docs/modules.md](./docs/modules.md)
- 설정 레퍼런스: [docs/configuration.md](./docs/configuration.md)
- API 가이드: [docs/api.md](./docs/api.md)
- 보안 동작: [docs/security.md](./docs/security.md)
- SPI 확장 가이드: [docs/extension-guide.md](./docs/extension-guide.md)
- 테스트/CI 가이드: [docs/testing-and-ci.md](./docs/testing-and-ci.md)
- 릴리즈 가이드: [docs/release.md](./docs/release.md)
- 트러블슈팅: [docs/troubleshooting.md](./docs/troubleshooting.md)
- RefreshCookieWriter 상세: [docs/refresh-cookie-writer.md](./docs/refresh-cookie-writer.md)

---

## 📦 모듈 (Modules)
> 각 모듈은 독립적으로 배포되며, 필요한 것만 선택해 사용할 수 있습니다.
> 현재 단계에서는 패키지명(`com.auth.api`, `com.auth.config`)은 유지하고, 모듈명만 `contract`, `starter`로 사용합니다.

| Module | 설명                                      |
|-------|-----------------------------------------|
| `contract` | 외부에 노출되는 모델, 예외                          |
| `core` | 인증 도메인 로직 (비즈니스 규칙)                     |
| `spi` | 사용자 저장소, 토큰 저장소 등 확장 포인트                |
| `starter` | Spring Boot 연동 설정 (AutoConfiguration, 엔드포인트, DTO) |
| `common` | 모듈 간 공용 유틸리티 메서드                          |

---

## 🚀 시작하기

### 1️⃣ GitHub Packages 설정

`settings.gradle` 또는 `build.gradle`에 GitHub Packages 저장소를 추가합니다.

```gradle
repositories {
    mavenCentral()
    maven {
        url = uri("https://maven.pkg.github.com/jho951/auth")
        credentials {
            username = project.findProperty("gprUser") ?: System.getenv("GITHUB_ACTOR")
            password = project.findProperty("gprKey") ?: System.getenv("GITHUB_TOKEN")
        }
    }
}

dependencies {
    implementation("io.github.jho951:auth-contract:1.0.9")
    implementation("io.github.jho951:auth-core:1.0.9")
    implementation("io.github.jho951:auth-spi:1.0.9")
    implementation("io.github.jho951:auth-starter:1.0.9")
    implementation("io.github.jho951:auth-common:1.0.9")
}
```
---

### 2️⃣ common 유틸 사용
> 자주 사용하는 메서드는 `auth-common`에 두고 각 모듈에서 import 해서 사용합니다.

```java
import com.auth.common.utils.Strings;

if (Strings.isBlank(username)) throw new IllegalArgumentException("username must not be blank");

String userId = Strings.requireNonBlank(rawUserId, "userId");
TokenService tokenService = Strings.requireNonNull(customTokenService, "tokenService");
```

---

### 3️⃣ application.yml 설정
> auth.jwt.secret가 존재하면 JWT 기반 TokenService가 자동 등록됩니다.

```yml
auth:
  refresh-cookie-name: "ADMIN_REFRESH_TOKEN"

  jwt:
    secret: ${AUTH_JWT_SECRET}
    access-seconds: 3600
    refresh-seconds: 1209600
```

`auth.jwt.refresh-seconds`는 다음 3곳에 동일하게 적용됩니다.
- Refresh JWT 만료 시간
- 서버 저장소의 Refresh Token TTL (`expiresAt`)
- Refresh 쿠키 `Max-Age`

### 4️⃣ UserFinder 구현 (필수)
> 각 서비스마다 사용자 저장 방식이 다르기 때문에 UserFinder는 반드시 애플리케이션에서 구현해야 합니다.
```java
// 예시
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

### 5️⃣ 로그인 API 사용
> auth-starter 모듈을 포함하면 다음 엔드포인트가 자동 제공됩니다. 

| Method | Path            | Description               |
| ------ | --------------- | ------------------------- |
| POST   | `/auth/login`   | 로그인 (access + refresh 발급) |
| POST   | `/auth/refresh` | access token 재발급          |
| POST   | `/auth/logout`  | refresh token 무효화         |




## 🔐 GitHub Actions Environment
> GitHub Actions에서는 **GITHUB_ACTOR**, **GITHUB_TOKEN**이 자동으로 제공됩니다.

>CI 환경에서는 별도의 시크릿 설정 없이 GitHub Packages에 안전하게 접근할 수 있습니다.

---

## 🛠 Build & Test
>프로젝트 빌드 및 테스트는 다음 명령어로 실행할 수 있습니다.

```bash
./gradlew clean build
```
---

### 🔐 Security Integration
> AuthOncePerRequestFilter가 자동으로 빈으로 등록됩니다.

```java
@Bean
SecurityFilterChain filterChain(HttpSecurity http,
		AuthOncePerRequestFilter authFilter) throws Exception {
	return http
		.csrf(csrf -> csrf.disable())
		.authorizeHttpRequests(auth -> auth
			.requestMatchers("/auth/**").permitAll()
			.anyRequest().authenticated()
		)
		.addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class)
		.build();
}
```

## 🏷 Release Policy
>릴리즈는 명확한 책임 분리를 원칙으로 합니다.

* 버전은 루트 `build.gradle`의 `version`에서 관리합니다.
* 태그(`v1.0.9`)는 직접 생성합니다. ***(현재 `v1.0.9`)***
* CI는 태그가 `push` 될 때만 `publish`를 수행합니다.

### 릴리즈 절차
```bash
git add -A                            
git commit -m "release: v1.0.9"
git tag -a v1.0.9 -m "release: v1.0.9"
git push origin main           
git push origin v1.0.9
```

## 📄 License
> [MIT LICENSE](./License)
