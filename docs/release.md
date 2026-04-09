# 릴리즈 가이드

## 현재 퍼블리싱 구조

루트 `build.gradle`은 각 서브프로젝트를 Central Portal 경유 Maven Central publish 경로로 연결합니다.
현재 artifactId는 `project.name`을 그대로 사용합니다.

버전과 group은 `gradle.properties`의 다음 키를 기준으로 읽습니다.

- 표준 키: `release_version`, `release_group`

예:

- `io.github.jho951:auth-core:<version>`
- `io.github.jho951:auth-jwt:<version>`
- `io.github.jho951:auth-session:<version>`
- `io.github.jho951:auth-hybrid:<version>`
- `io.github.jho951:auth-spring:<version>`
- `io.github.jho951:auth-spring-boot-starter:<version>`

## SCM 메타데이터

루트 `gradle.properties`의 `github_org`, `github_repo` 값을 기준으로 GitHub URL과 SCM 정보를 조립합니다.
`build.gradle`에 repo URL을 직접 하드코딩하지 않습니다.

## 버전 주입

릴리스 태그에서 실제 publish 버전은 `release_version`으로 주입하는 것을 권장합니다.

## 트리거

- Git tag `v*` push
- 현재 릴리스 예시: `v1.0`

## 환경 변수 / 시크릿

- `MAVEN_CENTRAL_USERNAME` - Central Portal user token username
- `MAVEN_CENTRAL_PASSWORD` - Central Portal user token password
- `MAVEN_CENTRAL_GPG_PRIVATE_KEY`
- `MAVEN_CENTRAL_GPG_PASSPHRASE`

## 릴리즈 절차 예시

```bash
git add -A
git commit -m "release: v1.0"
git tag -a v1.0 -m "release: v1.0"
git push origin main
git push origin v1.0
```

## 주의사항

- `auth-common-test`는 테스트 전용 모듈이라 publish 대상이 아닙니다.
- 현재 publish는 `artifactId = project.name` 규칙을 따릅니다.
