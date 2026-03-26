# Module: common

## 역할

`common`은 여러 모듈에서 사용하는 **작은 공용 유틸**을 담습니다.

## 포함 클래스

- `com.auth.common.utils.Strings`
- `com.auth.common.utils.MoreObjects`

## 사용 예

### `Strings`

- `isBlank`
- `requireNonBlank`
- `requireNonNull`
- `toStringOrNull`

### `MoreObjects`

- `defaultIfNull`

## 설계 의도

현재 저장소는 검증 로직과 간단한 null-safe 유틸을 `common`으로 분리해 재사용합니다.
다만 장기적으로는 `contract`가 `common` 의존 없이도 유지되도록 더 줄일 여지가 있습니다.
