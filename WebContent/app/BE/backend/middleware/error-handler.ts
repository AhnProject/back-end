// ============================================================
// Error Handler ??GlobalExceptionHandler.java (@RestControllerAdvice) ?댁떇
// 紐⑤뱺 Route Handler?먯꽌 怨듯넻?쇰줈 ?ъ슜?섎뒗 ?먮윭 泥섎━ ?섑띁
// ============================================================

import { NextResponse } from "next/server";
import { AppError } from "@/app/BE/backend/utils/app-error";
import { ZodError } from "zod";
import { error as apiError } from "@/app/BE/backend/utils/api-response";

/**
 * Route Handler ?먮윭 ???쒖? NextResponse 蹂??
 * Java: @ExceptionHandler(Exception.class) ?????
 */
export function handleRouteError(err: unknown): NextResponse {
  // AppError (AuthException + DocumentException ?듯빀)
  if (err instanceof AppError) {
    console.error(`[AppError] [${err.errorCode}] ${err.message}`);
    return NextResponse.json(apiError(err.errorCode, err.message), {
      status: err.httpStatus,
    });
  }

  // Zod Validation ?먮윭 (MethodArgumentNotValidException ???
  if (err instanceof ZodError) {
    const message = err.errors.map((e) => `${e.path.join(".")}: ${e.message}`).join(", ");
    console.warn(`[ValidationError] ${message}`);
    return NextResponse.json(apiError("INVALID_INPUT", `Validation failed: ${message}`), {
      status: 400,
    });
  }

  // 洹???Unknown ?먮윭
  console.error("[UnhandledError]", err);
  return NextResponse.json(apiError("INTERNAL_SERVER_ERROR", "Internal server error"), {
    status: 500,
  });
}



