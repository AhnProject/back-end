# ReeL-Trip

AI 기반 여행지 추천 서비스 — Turborepo 모노레포

## 기술 스택

| 영역 | 기술 |
|------|------|
| 웹 FE | Next.js 15 (App Router) |
| 앱 FE | React Native (Expo) |
| BE | NestJS + Prisma |
| DB | PostgreSQL + pgvector |
| AI | OpenAI (GPT-4o, text-embedding-3-small) |
| 모노레포 | Turborepo |
| 배포 | Vercel (web) · Railway (api) · EAS (mobile) |

## 프로젝트 구조

```
reel-trip/
├── apps/
│   ├── web/        # Next.js 웹 FE
│   ├── api/        # NestJS BE (MVC)
│   └── mobile/     # React Native (Expo)
├── packages/
│   ├── types/      # 공유 타입 정의
│   └── utils/      # 공유 유틸함수
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

```bash
# BE
cp apps/api/.env.example apps/api/.env

# Web
cp apps/web/.env.example apps/web/.env.local

# Mobile
cp apps/mobile/.env.example apps/mobile/.env.local
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

## API 문서

NestJS 서버 실행 후:
- Swagger UI: http://localhost:4000/api/docs

## 배포

### Web (Vercel)
- Root Directory: `apps/web`
- Build Command: `next build`

### API (Railway)
- Root Directory: `apps/api`
- Build Command: `nest build`
- Start Command: `node dist/main`

### Mobile (EAS)
```bash
cd apps/mobile
eas build --platform all
```
