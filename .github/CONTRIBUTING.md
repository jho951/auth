# Contributing Guide

> Auth Module에 기여해주셔서 감사합니다 🙌  
이 문서는 코드 기여 시 지켜야 할 기본 원칙과 규칙을 설명합니다.

---

## 🧭 Project 목표

이 프로젝트는 **재사용 가능한 인증 라이브러리**를 목표로 합니다.

따라서 다음 원칙을 지향합니다.

- 서비스 종속 로직 금지
- 인증과 사용자 저장소(User DB)의 명확한 분리
- 인터페이스(SPI) 중심 설계
- Spring Boot AutoConfiguration 기반 확장성

---

## 📦 Module Responsibility

| Module | Responsibility |
|------|---------------|
| auth-core | 순수 도메인 모델, 공통 로직 |
| auth-spi | 서비스별 구현이 필요한 인터페이스 |
| auth-config | 기본 구현 + AutoConfiguration |
| auth-api | 인증 HTTP API |

❗ `auth-core`에는 **Spring, JWT, DB 의존성 추가 금지**

---

## 🧑‍💻 Coding Rules

### Java
- Java 17+
- 불변 객체 우선
- 생성자에서 유효성 검증
- `Optional`은 반환용으로만 사용

### Exception
- `RuntimeException` 직접 사용 ❌
- `AuthException + ErrorCode` 사용 ⭕

---

## ⚙️ Configuration Rules

- 모든 설정은 `@ConfigurationProperties` 사용
- prefix는 `auth.*` 하위로 제한
- yml에 민감 정보(secret) 직접 작성 금지
  - 환경 변수 사용 권장

---

## 🔌 SPI 규칙

- SPI 인터페이스는 반드시 `auth-spi`에 위치
- 구현체는 **절대 auth 모듈 안에 두지 않음**
- 구현 예시는 테스트 또는 README에만 포함

---

## 🧪 Testing

- core 로직은 단위 테스트 필수
- AutoConfiguration은 context load 테스트 권장
- 외부 시스템(DB, Redis 등) 의존 테스트 금지

---

## 📝 Commit Convention (권장)
- feat: add jwt refresh token rotation
- fix: handle invalid token type
- refactor: extract AuthService
- docs: update README

---

## 📬 Issues & PR

- 기능 추가 전 Issue 등록 권장
- PR에는 반드시 변경 목적과 영향 범위 명시
- 공용 API 변경 시 Breaking Change 여부 명확히 표시

---
