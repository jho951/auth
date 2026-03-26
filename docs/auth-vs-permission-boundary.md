# auth vs permission 경계

이 저장소는 **인증(authentication)** 을 다루며, **인가(authorization)** 는 별도 계층에서 처리하는 것을 원칙으로 합니다.

## 핵심 구분표

| 항목 | auth 모듈 | permission 계층 |
|---|---|---|
| 핵심 질문 | 이 요청자는 누구인가? 인증되었는가? | 이 요청을 허용할 것인가? |
| 책임 | 토큰/세션 검증, principal 생성 | 리소스 접근 정책, 소유권, 멤버십, ACL 해석 |
| 입력 | JWT, refresh token, OAuth2 identity, cookie | principal + resource + action + domain policy |
| 출력 | `Principal`, `Tokens`, 인증 컨텍스트 | Allow / Deny / 403 decision |
| 다루는 데이터 | `userId`, `roles`, `claims` | `documentId`, `workspaceId`, `ownerId`, sharing state |
| 실패 코드 | 보통 401 | 보통 403 |

## 이 저장소가 해도 되는 것

- 토큰 발급과 검증
- `Principal` 생성
- `roles` 같은 메타데이터를 token claim으로 운반
- Spring Security 컨텍스트에 현재 사용자 연결

## 이 저장소가 하면 안 되는 것

- `canEditDocument(documentId)` 같은 리소스 정책 판단
- owner/editor/viewer 해석
- 워크스페이스 관리자 여부 판정
- 문서/블록/조직 도메인별 권한 정책 엔진

## 현재 코드에서 주의할 점

현재 `Principal`에는 `roles`와 `hasRole()`가 있습니다.
이 convenience API가 존재하더라도, **이 저장소가 리소스 authorization까지 책임진다는 뜻은 아닙니다.**

현재 구현에서 `AuthOncePerRequestFilter`는 `Principal.roles`를 `GrantedAuthority`로 변환합니다.
이것은 Spring Security 브릿지일 뿐이며, 최종 resource permission decision은 여전히 애플리케이션 또는 별도 permission 계층이 가져야 합니다.

## 장기 방향

장기적으로는 auth 쪽에서 role을 **authority metadata** 로만 다루고, role 의미 해석과 최종 allow/deny 판단은 permission 계층으로 더 명확히 분리하는 것이 바람직합니다.
