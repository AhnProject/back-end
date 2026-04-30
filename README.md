# ReeL-Trip

AI 기반 여행지 추천 서비스 — Turborepo 모노레포

## 기술 스택

| 영역 | 기술 |
|------|------|
| 웹 FE | Next.js 15 (App Router) |
| 앱 FE | React Native (Expo) |
| BE | Next.js 15 Route Handler + Prisma |
| DB | PostgreSQL + pgvector (Neon) |
| AI | OpenAI (GPT-4o, text-embedding-3-small) |
| 모노레포 | Turborepo |
| 배포 | Vercel (api · web) · EAS (mobile) |

## 프로젝트 구조

```
reel-trip/
├── apps/
│   ├── api/                        # Next.js API 서버
│   │   └── src/
│   │       ├── app/api/            # Route Handlers (Controller)
│   │       │   ├── auth/
│   │       │   │   ├── signup/route.ts
│   │       │   │   └── login/route.ts
│   │       │   ├── documents/
│   │       │   │   ├── route.ts
│   │       │   │   ├── health/route.ts
│   │       │   │   ├── search/route.ts
│   │       │   │   └── [id]/route.ts
│   │       │   └── recommend/route.ts
│   │       ├── auth/               # 인증 서비스 & 레포지토리
│   │       ├── document/           # 문서 서비스 & 레포지토리
│   │       ├── recommend/          # 추천 서비스
│   │       ├── ai/                 # OpenAI 서비스
│   │       ├── prisma/             # Prisma 싱글톤
│   │       ├── common/
│   │       │   ├── errors/         # 에러 클래스
│   │       │   ├── filters/        # 응답 헬퍼
│   │       │   └── guards/         # JWT 검증
│   │       └── middleware.ts       # CORS
│   ├── web/                        # Next.js 웹 FE
│   │   └── src/
│   │       ├── app/
│   │       │   ├── test-ui/        # API 테스트 UI
│   │       │   └── BE/             # API 문서 (Scalar)
│   │       └── lib/
│   │           └── api-client.ts   # API 호출 유틸
│   └── mobile/                     # React Native (Expo)
│       └── app/
│           ├── (tabs)/
│           └── auth/
├── packages/
│   ├── types/                      # 공유 타입 정의
│   └── utils/                      # 공유 유틸함수 (vector)
├── turbo.json
└── package.json
```

## 시작하기

### 필수 환경
- Node.js 20+
- npm 10+

### 설치

```bash
npm install
```

### 환경변수 설정

**apps/api/.env**
```env
DATABASE_URL=postgresql://USER:PASSWORD@HOST:5432/DATABASE?sslmode=require
JWT_SECRET=replace-with-a-long-random-secret-min-32-chars
JWT_EXPIRATION=86400000
OPENAI_API_KEY=sk-proj-xxxx
ALLOWED_ORIGINS=http://localhost:3000,http://localhost:8081
```

**apps/web/.env.local**
```env
NEXT_PUBLIC_API_URL=http://localhost:4000
```

**apps/mobile/.env.local**
```env
EXPO_PUBLIC_API_URL=http://localhost:4000
```

### 개발 서버 실행

```bash
# 전체 동시 실행
npm run dev

# 개별 실행
cd apps/api && npm run dev     # http://localhost:4000
cd apps/web && npm run dev     # http://localhost:3000
cd apps/mobile && npm run dev  # Expo Go
```

### DB 마이그레이션

```bash
cd apps/api
npx prisma db push
```

## API 엔드포인트

| Method | Path | 인증 | 설명 |
|--------|------|------|------|
| POST | /api/auth/signup | - | 회원가입 |
| POST | /api/auth/login | - | 로그인 |
| GET | /api/documents/health | - | 헬스체크 |
| GET | /api/documents | JWT | 문서 목록 |
| POST | /api/documents | JWT | 문서 생성 |
| GET | /api/documents/:id | JWT | 문서 조회 |
| PUT | /api/documents/:id | JWT | 문서 수정 |
| DELETE | /api/documents/:id | JWT | 문서 삭제 |
| POST | /api/documents/search | JWT | 벡터 유사도 검색 |
| GET | /api/recommend | - | 헬스체크 |
| POST | /api/recommend | - | AI 여행 추천 |

## 배포

### API (Vercel)
- Vercel 프로젝트 Root Directory: `apps/api`
- 환경변수: `DATABASE_URL`, `JWT_SECRET`, `OPENAI_API_KEY`, `ALLOWED_ORIGINS`

### Web (Vercel)
- Vercel 프로젝트 Root Directory: `apps/web`
- 환경변수: `NEXT_PUBLIC_API_URL`

### Mobile (EAS)
```bash
cd apps/mobile
eas build --platform all
```
