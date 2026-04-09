# auth

`auth`는 Java 17 기반의 인증 OSS 모듈입니다.
Spring 애플리케이션에서 `principal`, `token`, `session`, `JWT`, `OAuth2` 연동을 한 묶음으로 제공합니다.

[![Build](https://github.com/jho951/auth/actions/workflows/build.yml/badge.svg)](https://github.com/jho951/auth/actions/workflows/build.yml)
[![Publish](https://github.com/jho951/auth/actions/workflows/publish.yml/badge.svg)](https://github.com/jho951/auth/actions/workflows/publish.yml)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.jho951/auth-spring-boot-starter?label=maven%20central)](https://central.sonatype.com/search?q=io.github.jho951)
[![License](https://img.shields.io/github/license/jho951/auth)](./LICENSE)
[![Tag](https://img.shields.io/github/v/tag/jho951/auth)](https://github.com/jho951/auth/tags)

## 공개 좌표

- `io.github.jho951:auth-core`
- `io.github.jho951:auth-jwt`
- `io.github.jho951:auth-session`
- `io.github.jho951:auth-hybrid`
- `io.github.jho951:auth-spring`
- `io.github.jho951:auth-spring-boot-starter`

## 무엇을 제공하나

- `auth-core`: 공개 모델과 SPI
- `auth-jwt`: JWT 발급과 검증
- `auth-session`: 세션 기반 인증
- `auth-hybrid`: JWT와 세션 조합
- `auth-spring`: Spring 설정 바인딩
- `auth-spring-boot-starter`: 자동 구성과 기본 구현

## 버전 정책

- 배포 기준 버전은 `release_version`입니다.
- 배포 group은 `release_group`입니다.
- 태그 push 시 릴리스 버전이 주입되어 Maven Central에 publish합니다.

## 공개 전 체크

- [OSS 공개 체크리스트](docs/oss-readiness.md)
- [기여 가이드](CONTRIBUTING.md)

## 빠른 시작

```gradle
repositories {
    mavenCentral()
}

dependencies {
    implementation("io.github.jho951:auth-spring-boot-starter:<version>")
}
```

## 문서

- [docs/README.md](docs/README.md)
