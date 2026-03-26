# Module: auth-common-test

> 상태: **현재 구현 없음**

---

## 목적

`auth-common-test`는 모듈 전반에서 공통으로 사용하는 테스트 픽스처를 담는 테스트 전용 모듈입니다.

모듈 수가 늘어날수록 Fake 구현과 Fixture 코드가 반복되므로,
위키 차원에서 먼저 이 모듈의 존재를 고정해두는 것이 좋습니다.

---

## 포함 범위

- 테스트용 principal/user/tokens 빌더
- Fake `UserFinder`
- Fake `TokenService`
- Fake `RefreshTokenStore`
- 고정 Clock
- 만료된/유효한 토큰 시나리오 fixture
- 샘플 앱의 통합 테스트 지원

---

## 제외 범위

- 운영 코드
- AutoConfiguration
- 실제 JWT 서명 구현
- 프로덕션 환경용 저장소 구현

---

## 현재 저장소에서의 매핑

현재 저장소에는 `auth-common-test` 모듈이 없다.

즉 현재 테스트 유틸은 각 모듈 테스트에 분산되거나 아직 충분히 분리되어 있지 않다.

---

## 의존 관계

- 필수 의존:
  - `auth-core`
- 선택 의존:
  - 모듈별 test scope

---

## 공개 API / 대표 타입

### 제안 타입
- `TestClockFactory`
- `PrincipalFixtures`
- `UserFixtures`
- `FakeUserFinder`
- `FakeTokenService`
- `FakeRefreshTokenStore`

### 설계 원칙
- production artifact 와 분리한다.
- 모듈 단위 테스트와 sample integration test 모두에서 재사용 가능해야 한다.

---

## 완료 기준

- 모든 하위 모듈 테스트에서 중복 fixture 를 줄일 수 있어야 한다.
- production runtime 에 포함되지 않아야 한다.
