// ============================================================
// Error Handler — GlobalExceptionHandler.java (@RestControllerAdvice) 이식
// 모든 Route Handler에서 공통으로 사용하는 에러 처리 래퍼
// ============================================================

import { NextResponse } from "next/server";
import { AppError } from "@/lib/utils/app-error";
import { ZodError } from "zod";
import { error as apiError } from "@/lib/utils/api-response";

/**
 * Route Handler 에러 → 표준 NextResponse 변환
 * Java: @ExceptionHandler(Exception.class) 에 대응
 */
export function handleRouteError(err: unknown): NextResponse {
  // AppError (AuthException + DocumentException 통합)
  if (err instanceof AppError) {
    console.error(`[AppError] [${err.errorCode}] ${err.message}`);
    return NextResponse.json(apiError(err.errorCode, err.message), {
      status: err.httpStatus,
    });
  }

  // Zod Validation 에러 (MethodArgumentNotValidException 대응)
  if (err instanceof ZodError) {
    const message = err.errors.map((e) => `${e.path.join(".")}: ${e.message}`).join(", ");
    console.warn(`[ValidationError] ${message}`);
    return NextResponse.json(apiError("INVALID_INPUT", `Validation failed: ${message}`), {
      status: 400,
    });
  }

  // SyntaxError (잘못된 JSON 바디)
  if (err instanceof SyntaxError) {
    return NextResponse.json(apiError("INVALID_JSON", "Request body is not valid JSON"), {
      status: 400,
    });
  }

  // 그 외 Unknown 에러
  console.error("[UnhandledError]", err);
  return NextResponse.json(apiError("INTERNAL_SERVER_ERROR", "Internal server error"), {
    status: 500,
  });
}
