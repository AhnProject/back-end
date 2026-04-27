# Next.js API Migration

Spring Boot 백엔드를 폐기하고 Next.js App Router 기반 API 서버로 전환한 프로젝트입니다.  
배포 대상은 Vercel이며, 로컬에서는 Docker Compose로 pgvector PostgreSQL을 띄워 연결 상태와 API 흐름을 바로 확인할 수 있습니다.

## 프로젝트 위치

- Next.js 앱: [front-end](/C:/Github/back-end/front-end)
- 로컬 DB 설정: [docker-compose.yml](/C:/Github/back-end/docker-compose.yml)
- DB 초기화 SQL: [docker/db/init/01-init.sql](/C:/Github/back-end/docker/db/init/01-init.sql)

## 로컬 실행 순서

### 1. pgvector 실행

루트에서 실행:

```powershell
docker compose up -d
```

상태 확인:

```powershell
docker compose ps
```

기본 접속 정보:

- Host: `localhost`
- Port: `5432`
- Database: `aidb`
- Username: `postgres`
- Password: `postgres`

초기화 시 자동 생성:

- `vector` extension
- `users` 테이블
- `documents` 테이블

### 2. 환경변수 준비

```powershell
Copy-Item front-end\.env.local.example front-end\.env.local
```

최소 수정 항목:

- `DATABASE_URL`
- `JWT_SECRET`
- `OPENAI_API_KEY`

예시:

```env
OPENAI_API_KEY=sk-proj-xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
DATABASE_URL=postgresql://postgres:postgres@localhost:5432/aidb?schema=public
JWT_SECRET=local-development-secret-change-this
JWT_EXPIRATION=86400000
NEXT_PUBLIC_APP_URL=http://localhost:3000
VECTOR_DIMENSION=1536
```

### 3. Next.js 앱 실행

```powershell
cd front-end
npm install
npm run dev
```

개발 서버:

- `http://localhost:3000`

## 접속 URL

- 홈: `http://localhost:3000`
- 테스트 페이지: `http://localhost:3000/test-ui`
- API 문서 UI: `http://localhost:3000/api-docs`
- OpenAPI JSON: `http://localhost:3000/docs`
- 문서 헬스체크: `http://localhost:3000/api/documents/health`
- 추천 헬스체크: `http://localhost:3000/api/recommend`

## `/api-docs` 여는 방법

앱 실행 후 브라우저에서 아래 주소를 열면 됩니다.

```text
http://localhost:3000/api-docs
```

원본 스펙 JSON은:

```text
http://localhost:3000/docs
```

## 실데이터가 없어도 가능한 테스트

현재 실데이터가 없어도 아래는 바로 확인할 수 있습니다.

- 회원가입 `/api/auth/signup`
- 로그인 `/api/auth/login`
- JWT 발급 여부
- 보호 API 인증 성공/실패 여부
- DB 연결 여부
- 문서 API 헬스체크
- docs 페이지 오픈 여부

주의:

- `/api/recommend`는 `OPENAI_API_KEY` 필요
- `/api/documents`의 `POST`도 임베딩 자동 생성 때문에 `OPENAI_API_KEY` 필요

즉, OpenAI 키가 없으면 추천과 문서 생성은 실패할 수 있지만, 인증과 DB 연결 테스트는 가능합니다.

## 가장 빠른 테스트 절차

1. 루트에서 `docker compose up -d`
2. `front-end/.env.local` 생성
3. `cd front-end`
4. `npm install`
5. `npm run dev`
6. `http://localhost:3000/test-ui` 접속
7. 회원가입 또는 로그인
8. `http://localhost:3000/api-docs` 접속

## DB 확인 명령

로그:

```powershell
docker compose logs -f db
```

psql 접속:

```powershell
docker compose exec db psql -U postgres -d aidb
```

확인용 SQL:

```sql
\dt
SELECT extname FROM pg_extension;
SELECT COUNT(*) FROM users;
SELECT COUNT(*) FROM documents;
```

종료:

```powershell
docker compose down
```

볼륨까지 삭제:

```powershell
docker compose down -v
```

## Vercel 설정 방법

### 1. 프로젝트 Import

Vercel에서 이 저장소를 Import 합니다.

중요:

- Root Directory를 `front-end`로 설정해야 합니다.

### 2. Build 설정

현재 [front-end/package.json](/C:/Github/back-end/front-end/package.json)에 맞춰 아래로 동작합니다.

- Install Command: `npm install`
- Build Command: `npm run build`
- Framework Preset: `Next.js`

별도 서버 명령은 필요 없습니다.

### 3. Vercel 환경변수 등록

Vercel Dashboard > Project > Settings > Environment Variables 에 등록:

- `DATABASE_URL`
- `OPENAI_API_KEY`
- `JWT_SECRET`
- `JWT_EXPIRATION`
- `NEXT_PUBLIC_APP_URL`

예시:

```env
DATABASE_URL=postgresql://USER:PASSWORD@HOST:5432/DATABASE?sslmode=require
OPENAI_API_KEY=sk-proj-xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
JWT_SECRET=replace-with-a-long-random-secret
JWT_EXPIRATION=86400000
NEXT_PUBLIC_APP_URL=https://your-project.vercel.app
```

### 4. 배포용 DB 주의사항

Vercel에서는 로컬 Docker DB를 사용할 수 없습니다.  
배포 시에는 외부 PostgreSQL이 필요합니다.

권장:

- Neon
- Vercel Postgres
- Supabase Postgres

필수:

- `pgvector extension` 지원
- SSL 연결 가능

### 5. 배포 후 확인 URL

예시 주소가 `https://example.vercel.app`라면:

- `https://example.vercel.app/test-ui`
- `https://example.vercel.app/api-docs`
- `https://example.vercel.app/docs`
- `https://example.vercel.app/api/documents/health`

## 빌드 확인

로컬에서 다음 빌드가 통과하도록 맞춰져 있습니다.

```powershell
cd front-end
npm run build
```
