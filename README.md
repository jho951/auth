# auth

`auth`는 Java 17 기반의 인증 OSS 모듈입니다.
`principal`, `token`, `session`, `JWT` 관련 핵심 모델과 구현을 한 묶음으로 제공합니다.

[![Build](https://github.com/jho951/auth/actions/workflows/build.yml/badge.svg)](https://github.com/jho951/auth/actions/workflows/build.yml)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.jho951/auth-core?label=maven%20central)](https://central.sonatype.com/search?q=jho951)
[![License](https://img.shields.io/badge/license-Apache%202.0-blue)](./LICENSE)
[![Tag](https://img.shields.io/github/v/tag/jho951/auth)](https://github.com/jho951/auth/tags)

## 공개 좌표

- `io.github.jho951:auth-core`
- `io.github.jho951:auth-jwt`
- `io.github.jho951:auth-session`
- `io.github.jho951:auth-hybrid`

## 무엇을 제공하나

- `auth-core`: 공통 모델과 연동 포인트
- `auth-jwt`: JWT 발급과 검증 구현
- `auth-session`: 세션 저장소와 세션 인증 로직
- `auth-hybrid`: JWT와 세션 조합 로직

- [기여 가이드](CONTRIBUTING.md)

## 빠른 시작

```gradle
repositories {
    mavenCentral()
}

dependencies {
    implementation("io.github.jho951:auth-core:<version>")
    implementation("io.github.jho951:auth-jwt:<version>")
    implementation("io.github.jho951:auth-session:<version>")
    implementation("io.github.jho951:auth-hybrid:<version>")
}
```

## 문서

- [docs/README.md](docs/README.md)
