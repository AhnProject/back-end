# 프로젝트 디렉토리 구조 정의 (도메인 중심 아키텍처)

이 문서는 효율적인 유지보수와 확장성을 위해 채택한 도메인(서비스) 중심의 MVC 패턴 폴더 구조를 정의합니다.

## 1. 핵심 원칙
- **응집도 향상:** 관련된 기능(Controller, Service, DTO 등)은 동일한 도메인 폴더 내에 위치한다.
- **결합도 분리:** 각 도메인은 독립적으로 동작할 수 있도록 설계하며, 공통 요소는 `global` 또는 `common`에서 관리한다.
- **탐색 용이성:** 특정 기능을 수정할 때 여러 폴더를 오가지 않고 해당 도메인 폴더 내에서 해결한다.

## 2. 권장 폴더 구조 (Directory Structure)

```text
src/
├── main/
│   ├── java/com/project/
│   │   ├── global/                # 공통 설정 및 유틸리티
│   │   │   ├── config/            # 보안, DB, Swagger 등 설정
│   │   │   ├── common/            # 공통 응답 객체, 공통 예외 처리
│   │   │   └── util/              # 공통 유틸 함수 (날짜, 문자열 등)
│   │   │
│   │   └── domains/               # 비즈니스 도메인 (서비스 단위)
│   │       ├── auth/              # 인증/인가 도메인
│   │       │   ├── controller/
│   │       │   ├── service/
│   │       │   ├── dto/           # Request, Response DTO
│   │       │   ├── repository/
│   │       │   └── entity/        # 도메인 전용 엔티티
│   │       │
│   │       ├── board/             # 게시판 도메인
│   │       │   ├── controller/
│   │       │   ├── service/
│   │       │   ├── dto/
│   │       │   ├── repository/
│   │       │   └── entity/
│   │       │
│   │       └── user/              # 사용자 관리 도메인
│   │           ├── controller/
│   │           ├── service/
│   │           └── ...
│   │
│   └── resources/                 # 설정 파일 (yml, xml 등)