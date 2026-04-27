// ============================================================
// AppError — AuthException + DocumentException 통합 이식
// Spring Boot의 커스텀 RuntimeException 패턴 → TypeScript 클래스
// ============================================================

export class AppError extends Error {
  constructor(
    message: string,
    public readonly errorCode: string,
    public readonly httpStatus: number
  ) {
    super(message);
    this.name = "AppError";
  }
}

// ─── Auth 에러 팩토리 (AuthException.java 대응) ───────────────
export const AuthError = {
  usernameAlreadyExists: (username: string) =>
    new AppError(`Username already exists: ${username}`, "USERNAME_ALREADY_EXISTS", 400),
  emailAlreadyExists: (email: string) =>
    new AppError(`Email already exists: ${email}`, "EMAIL_ALREADY_EXISTS", 400),
  invalidCredentials: () =>
    new AppError("Invalid username or password", "INVALID_CREDENTIALS", 401),
  userNotFound: (username: string) =>
    new AppError(`User not found: ${username}`, "USER_NOT_FOUND", 404),
  tokenExpired: () =>
    new AppError("Token expired", "TOKEN_EXPIRED", 401),
  invalidToken: () =>
    new AppError("Invalid token", "INVALID_TOKEN", 401),
  unauthorized: () =>
    new AppError("Authentication required", "UNAUTHORIZED", 401),
  accessDenied: () =>
    new AppError("Access denied", "ACCESS_DENIED", 403),
};

// ─── Document 에러 팩토리 (DocumentException.java 대응) ──────────
export const DocumentError = {
  notFound: (id: string | number) =>
    new AppError(`Document not found. ID: ${id}`, "DOCUMENT_NOT_FOUND", 404),
  invalidVectorDimension: (expected: number, actual: number) =>
    new AppError(
      `Invalid vector dimension. Expected: ${expected}, actual: ${actual}`,
      "INVALID_VECTOR_DIMENSION",
      400
    ),
  databaseError: (msg: string) =>
    new AppError(`Database error: ${msg}`, "DATABASE_ERROR", 500),
  invalidInput: (msg: string) =>
    new AppError(`Invalid input: ${msg}`, "INVALID_INPUT", 400),
};
