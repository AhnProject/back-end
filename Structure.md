# Project Structure Guide

이 문서는 현재 저장소의 실제 구조와 각 레이어의 역할을 설명합니다.
README와는 별개로, 개발자가 구조를 빠르게 이해하기 위한 기준 문서입니다.
 
## 1. Top-Level

- `WebContent/`: Next.js 애플리케이션 루트 (FE + BE)
- `docker/`: 로컬 PostgreSQL + pgvector 초기화 관련 파일
- `docker-compose.yml`: 로컬 DB 실행 설정
- `README.md`: 실행/배포 가이드
- `Structure.md`: 구조 설명(현재 문서)

## 2. App Router 기준 구조

```text
WebContent/
  app/
    FE/                          # 화면(프론트엔드)
      page.tsx
      test-ui/page.tsx

    BE/                          # 서버 기능(백엔드)
      api/                       # HTTP 엔드포인트(route.ts)
        auth/login/route.ts
        auth/signup/route.ts
        documents/route.ts
        documents/[id]/route.ts
        documents/search/route.ts
        documents/health/route.ts
        recommend/route.ts
      docs/route.ts
      api-docs/route.ts

      backend/                   # 백엔드 내부 레이어
        controllers/             # 요청 단위 흐름 제어
        services/                # 비즈니스 로직
        repositories/            # DB 접근
        models/                  # 타입/모델
        middleware/              # 인증/에러 핸들링
        validators/              # 입력 검증(Zod)
        utils/                   # 공통 유틸(JWT, 응답 포맷 등)
        prisma.ts
        openapi-spec.ts

    layout.tsx
    page.tsx                     # 루트 -> /test-ui 리다이렉트

  prisma/schema.prisma
  next.config.ts
  package.json
```

## 3. 왜 `api/route.ts`가 필요한가

Next.js(App Router)에서 HTTP 엔드포인트는 `app/**/route.ts` 파일로만 노출됩니다.
즉, `controllers/services/models`만으로는 URL이 열리지 않습니다.

역할 분리:

- `api/*/route.ts`: HTTP 입구(메서드 매핑, 요청 수신)
- `controller/service/repository/model`: MVC 내부 처리

요약하면 `api`는 MVC의 대체가 아니라, 프레임워크 라우터 레이어입니다.

## 4. 요청 처리 흐름 (FE -> BE -> Vercel)

1. 사용자가 FE 페이지 접속 (`/test-ui` 등)
2. FE에서 `fetch('/api/...')` 호출
3. `next.config.ts` rewrites가 내부 경로 `/BE/api/...`로 매핑
4. `BE/api/**/route.ts`가 요청 수신
5. `route.ts -> controller -> service -> repository(DB)` 순서로 처리
6. 공통 응답 포맷(JSON) 반환
7. FE가 응답을 받아 UI 갱신
8. 전체 실행은 Vercel의 Next.js 서버 런타임에서 처리

## 5. 현재 구조 원칙

- 물리적 분리: `app/FE`, `app/BE`
- 백엔드 패턴: `backend/controllers|services|repositories|models`
- 공통 기능: `middleware|validators|utils`로 분리
- 외부 URL 안정성: rewrites로 `/api`, `/docs`, `/api-docs`, `/test-ui` 유지

## 6. 유지보수 규칙

- 새로운 엔드포인트 추가 시:
  - `app/BE/api/.../route.ts` 생성
  - 필요한 controller/service/repository만 추가
- 비즈니스 로직은 route.ts에 두지 않고 service로 이동
- DB 쿼리는 repository에만 위치
- 입력 검증은 validators(Zod)에서 우선 처리
- 인증/권한은 middleware로 공통 처리
