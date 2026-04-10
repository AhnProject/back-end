# Document Vector Search API with JWT Authentication

Spring Boot 기반의 문서 벡터 검색 및 JWT 인증 API 서버입니다.

## 주요 기능

- ✅ JWT 기반 사용자 인증 (회원가입/로그인)
- ✅ 문서 CRUD 기능
- ✅ PostgreSQL pgvector를 활용한 벡터 유사도 검색
- ✅ Swagger/OpenAPI 자동 문서화
- ✅ MVC 3-Layer 아키텍처 (Controller-Service-Repository)
- ✅ 완전한 예외 처리 및 검증

## 기술 스택

- **Language**: Java 17
- **Framework**: Spring Boot 3.5.11
- **Security**: Spring Security + JWT
- **Database**: PostgreSQL 16 + pgvector
- **Persistence**: JDBC Template
- **Documentation**: Swagger/OpenAPI 3
- **Build**: Gradle

## 빠른 시작

### 1. 데이터베이스 설정

```bash
# Docker로 PostgreSQL + pgvector 실행
docker run --name postgres-vector \
  -e POSTGRES_PASSWORD=pgPasswd \
  -p 5432:5432 \
  -d pgvector/pgvector:pg16

# 데이터베이스 초기화
docker exec -i postgres-vector psql -U postgres < init-db.sql
```

### 2. 애플리케이션 실행

```bash
# Gradle로 빌드 및 실행
./gradlew bootRun

# 또는 JAR 파일 생성 후 실행
./gradlew build
java -jar build/libs/back-end-0.0.1-SNAPSHOT.jar
```

### 3. Swagger UI 접속

브라우저에서 다음 URL로 접속:
```
http://localhost:8080/swagger-ui.html
```

## API 문서

모든 API는 **Swagger UI**에서 테스트 가능합니다. 아래는 주요 엔드포인트 목록입니다.

### 🔐 인증 API

| Method | Endpoint | 설명 | 인증 필요 |
|--------|----------|------|-----------|
| POST | `/api/auth/signup` | 회원가입 | ❌ |
| POST | `/api/auth/login` | 로그인 | ❌ |

### 📄 문서 API

| Method | Endpoint | 설명 | 인증 필요 |
|--------|----------|------|-----------|
| POST | `/api/documents` | 문서 생성 | ✅ |
| GET | `/api/documents/{id}` | 문서 조회 | ✅ |
| GET | `/api/documents` | 전체 문서 조회 | ✅ |
| PUT | `/api/documents/{id}` | 문서 수정 | ✅ |
| DELETE | `/api/documents/{id}` | 문서 삭제 | ✅ |
| POST | `/api/documents/search` | 벡터 검색 | ✅ |
| GET | `/api/documents/health` | 헬스 체크 | ✅ |

## 사용 방법

### 1️⃣ 회원가입

**Swagger UI에서:**
1. `인증` 섹션 펼치기
2. `POST /api/auth/signup` 클릭
3. "Try it out" 버튼 클릭
4. 아래 JSON 예제 복사하여 붙여넣기:

```json
{
  "username": "john_doe",
  "password": "password123!",
  "email": "john@example.com"
}
```

5. "Execute" 버튼 클릭
6. 응답에서 `accessToken` 복사

### 2️⃣ JWT 토큰 설정

**Swagger UI 우측 상단:**
1. "Authorize" 버튼 클릭 (🔓 자물쇠 아이콘)
2. 복사한 토큰 붙여넣기 (Bearer 접두어 없이)
3. "Authorize" 버튼 클릭
4. "Close" 버튼 클릭

이제 모든 문서 API를 사용할 수 있습니다!

### 3️⃣ 문서 생성

**Swagger UI에서:**
1. `문서` 섹션 펼치기
2. `POST /api/documents` 클릭
3. "Try it out" 버튼 클릭
4. 아래 JSON 예제 복사하여 붙여넣기:

```json
{
  "title": "첫 번째 문서",
  "content": "이것은 테스트 문서입니다.",
  "embedding": [0.1, 0.2, 0.3, 0.4, 0.5]
}
```

5. "Execute" 버튼 클릭

### 4️⃣ 벡터 검색

**Swagger UI에서:**
1. `POST /api/documents/search` 클릭
2. "Try it out" 버튼 클릭
3. 아래 JSON 예제 복사하여 붙여넣기:

```json
{
  "embedding": [0.2, 0.3, 0.4, 0.5, 0.6],
  "limit": 10,
  "threshold": 0.7
}
```

4. "Execute" 버튼 클릭

## 환경 설정

`application.yml` 파일에서 설정 변경 가능:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/vector_db
    username: postgres
    password: pgPasswd

server:
  port: 8080

jwt:
  secret: your-secret-key-minimum-256-bits
  expiration: 86400000  # 24시간
```

## 데이터베이스 스키마

### users 테이블
```sql
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    role VARCHAR(20) DEFAULT 'USER',
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP
);
```

### documents 테이블
```sql
CREATE TABLE documents (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    embedding vector(1536),
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP
);
```

## 프로젝트 구조

```
src/main/java/com/devahn/
├── config/              # 설정 (Security, Swagger)
├── controller/          # REST API 컨트롤러
├── service/             # 비즈니스 로직
├── repository/          # 데이터 액세스
├── domain/              # 엔티티 (User, Document)
├── dto/                 # 요청/응답 DTO
├── security/            # JWT 인증/인가
├── exception/           # 예외 처리
├── common/              # 공통 컴포넌트
└── util/                # 유틸리티
```

## 트러블슈팅

### Swagger 500 에러 발생 시
```bash
# 의존성 재다운로드
./gradlew clean build --refresh-dependencies
```

### JWT 토큰 만료 시
로그인 API를 다시 호출하여 새 토큰 발급

### 데이터베이스 연결 실패 시
```bash
# PostgreSQL 컨테이너 상태 확인
docker ps | grep postgres-vector

# 로그 확인
docker logs postgres-vector
```

## 라이선스

MIT License

## 개발자

DevAhn - 	dev.ahnjk@gmail.com
