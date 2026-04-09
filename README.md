# auth

`auth`는 Java 17 기반의 인증 OSS 모듈입니다.
Spring 애플리케이션에서 `principal`, `token`, `session`, `JWT`, `OAuth2` 연동을 한 묶음으로 제공합니다.

[![Build](https://github.com/jho951/auth/actions/workflows/build.yml/badge.svg)](https://github.com/jho951/auth/actions/workflows/build.yml)
[![Publish](https://github.com/jho951/auth/actions/workflows/publish.yml/badge.svg)](https://github.com/jho951/auth/actions/workflows/publish.yml)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.jho951/auth-core?label=maven%20central)](https://central.sonatype.com/search?q=io.github.jho951)
[![License](https://img.shields.io/badge/license-Apache%202.0-blue)](./LICENSE)
[![Tag](https://img.shields.io/github/v/tag/jho951/auth)](https://github.com/jho951/auth/tags)

## 공개 좌표

- `io.github.jho951:auth-core`
- `io.github.jho951:auth-jwt`
- `io.github.jho951:auth-session`
- `io.github.jho951:auth-hybrid`
- `io.github.jho951:auth-spring`

## 무엇을 제공하나

- `auth-core`: 공통 모델과 연동 포인트
- `auth-jwt`: JWT 발급과 검증 구현
- `auth-session`: 세션 기반 인증 구현
- `auth-hybrid`: JWT와 세션 조합 구현
- `auth-spring`: Spring 설정 바인딩과 어댑터

- [기여 가이드](CONTRIBUTING.md)

## 빠른 시작

```gradle
repositories {
    mavenCentral()
}

dependencies {
    implementation("io.github.jho951:auth-core:<version>")
    implementation("io.github.jho951:auth-jwt:<version>")
    implementation("io.github.jho951:auth-spring:<version>")
}
```

## 문서

- [docs/README.md](docs/README.md)
