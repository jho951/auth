# Auth Modules (Java / Spring-friendly)

이 저장소는 인증 기능을 **api / core / spi / config** 모듈로 분리한  
재사용 가능한 Java 인증 라이브러리 모음입니다.

- 🔐 인증 도메인 로직 분리
- 🧩 SPI 기반 확장 구조
- 📦 GitHub Packages 배포
- 🚀 CI 기반 자동 빌드 / 태그 기반 릴리즈

---

## 📦 Modules

| Module        | Description |
|---------------|-------------|
| `auth-api`    | 외부에 노출되는 모델, DTO, 예외 |
| `auth-core`   | 인증 도메인 로직 (비즈니스 규칙) |
| `auth-spi`    | 사용자 저장소, 토큰 저장소 등 확장 포인트 |
| `auth-config` | Spring Boot 연동 설정 (AutoConfiguration 등) |

각 모듈은 독립적으로 배포되며, 필요한 것만 선택해 사용할 수 있습니다.

---

## 🧱 Project Structure

``` text
├─ api/
├─ core/
├─ spi/
├─ config/
├─ gradle/
├─ build.gradle
├─ gradle.properties
└─ settings.gradle
```

---

## 🚀 Getting Started

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
```

## GitHub Actions에서는 GITHUB_ACTOR, GITHUB_TOKEN이 자동으로 제공됩니다.

## 🔐 GitHub Actions Environment

* `GITHUB_ACTOR`
* `GITHUB_TOKEN`

따라서 CI 환경에서는 별도의 시크릿 설정 없이
GitHub Packages에 안전하게 접근할 수 있습니다.

---

## 2️⃣ 의존성 추가

Gradle 프로젝트에서 아래와 같이 의존성을 추가하세요.

```gradle
dependencies {
    implementation("io.github.jho951:auth-api:1.0.0")
    implementation("io.github.jho951:auth-core:1.0.0")
    implementation("io.github.jho951:auth-spi:1.0.0")
    implementation("io.github.jho951:auth-config:1.0.0")
}
```

---

## 🛠 Build & Test

프로젝트 빌드 및 테스트는 다음 명령어로 실행할 수 있습니다.

```bash
./gradlew clean build
```

---

## 🏷 Release Policy

릴리즈는 명확한 책임 분리를 원칙으로 합니다.

* 버전은 `gradle.properties` 파일에서 관리합니다.
* 태그(`v1.0.0`)는 사람이 직접 생성합니다.
* CI는 태그가 푸시될 때만 publish를 수행합니다.

### 릴리즈 절차

```bash
git add -A                            
git commit -m "release: v현재 릴리즈 버전"
git tag -a v1.0.0 -m "release: v현재 릴리즈 버전"
git push origin main           
git push origin v릴리즈 버전

```
