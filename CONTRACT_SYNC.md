# CONTRACT_SYNC - auth

이 문서는 `auth` 레포를 `oss-contract` 기준에 맞춰 유지할 때 확인하는 체크리스트다.

## 기준

- 기준 SOT: `oss-contract`
- 대상 레포: `auth`

## 버전 기준

- 기본 버전 SOT는 `gradle.properties`의 `release_version`이다.
- 배포 group은 `release_group`이다.
- `build.gradle`의 하드코딩 fallback은 두지 않음

## 확인된 역할

- 인증 OSS 라이브러리
- 공개 API, artifact 좌표, 릴리스 규칙, 사용 예시 제공
- principal / token / session 중심
- JWT / OAuth2 / Spring 연동 분리

## 반영 문서

- `README.md`
- `docs/README.md`
- `docs/release.md`
- `docs/testing-and-ci.md`
- `docs/extension-guide.md`
- `docs/oauth2-design.md`
- `gradle.properties`
- `build.gradle`
- `settings.gradle`
- `.github/workflows/publish.yml`
