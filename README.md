# Document Vector Search API

Spring Boot backend for JWT authentication, document CRUD, and pgvector-based similarity search.

## Structure

```text
src/main/java/com/devahn
  app/
    api/
    config/
    security/
  auth/
    controller/
    domain/
    dto/
    repository/
    service/
  document/
    controller/
    domain/
    dto/
    repository/
    service/
  support/
    exception/
    util/
```

## Local run

Run the app with a Java 17 runtime:

```bash
./gradlew bootRun
```

Default local profile values:

- `SPRING_PROFILES_ACTIVE=local`
- `DB_HOST=localhost`
- `DB_PORT=5432`
- `DB_NAME=vector_db`
- `DB_USERNAME=postgres`
- `DB_PASSWORD=pgPasswd`
- `JWT_SECRET=change-this-secret-key-for-local-development-1234567890`

Swagger UI:

```text
http://localhost:8080/swagger-ui.html
```

## Docker

Start the app and database together:

```bash
docker compose up --build
```

Services:

- `db`: PostgreSQL 16 with pgvector
- `app`: Spring Boot service using the `docker` profile

## Tests

```bash
./gradlew test
```

Tests use an in-memory H2 datasource and do not require PostgreSQL.
