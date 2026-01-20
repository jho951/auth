# Contributing Guide

이 프로젝트에 관심을 가져주셔서 감사합니다 🙌
본 문서는 **일관된 기여 방식과 안정적인 릴리즈**를 위해 작성되었습니다.

---

## 🧭 기본 원칙

* 단순함(Simple)
* 명확한 책임 분리(Modular)
* CI 친화적 구조
* 하위 호환성 고려

---

## 🌿 Branch Strategy

| Branch       | Purpose        |
| ------------ | -------------- |
| `main`       | 안정 버전 / 릴리즈 기준 |
| `feature/*`  | 기능 개발          |
| `fix/*`      | 버그 수정          |
| `refactor/*` | 구조 개선          |

> ❗ `main` 브랜치에 직접 push하지 마세요.
> 모든 변경은 Pull Request를 통해 병합합니다.

---

## 🔀 Pull Request 규칙

PR 생성 시 아래 사항을 지켜주세요.

* 변경 목적이 명확할 것
* 하나의 PR은 하나의 책임만 포함할 것
* CI 빌드가 통과해야 병합 가능

### PR 제목 예시

* `feat: add token rotation strategy`
* `fix: handle null credentials in publish`
* `refactor: simplify auth-core structure`

---

## 🧪 Build & Test

PR 제출 전 아래 명령이 성공해야 합니다.

```bash
./gradlew clean build
```

> 테스트가 추가되었다면 반드시 테스트 코드도 함께 포함해주세요.

---

## 🧱 모듈별 가이드

### auth-api

* 외부에 노출되는 모델, DTO, 예외만 포함
* Spring / DB 의존성 금지

### auth-core

* 순수 비즈니스 로직 담당
* `auth-spi` 인터페이스에만 의존

### auth-spi

* 외부 시스템 연동을 위한 계약(SPI)
* 구현체는 포함하지 않음

### auth-config

* Spring Boot 연동 모듈
* AutoConfiguration 및 Bean 정의

---

## 🏷 Versioning Policy

Semantic Versioning을 따릅니다.

* `MAJOR.MINOR.PATCH`
* Breaking Change → MAJOR 증가
* 기능 추가 → MINOR 증가
* 버그 수정 → PATCH 증가

버전은 `gradle.properties` 파일에서 관리합니다.

---

## 🚫 금지 사항

* 순환 의존성 추가
* `auth-api` → `auth-core` 참조
* `auth-config`에서 core 내부 구현 직접 접근
* 시크릿 값 하드코딩 또는 커밋

---

## 💬 커뮤니케이션

* 버그 및 제안 사항은 Issue로 등록해주세요.
* PR 코멘트 및 리뷰는 언제든 환영합니다.