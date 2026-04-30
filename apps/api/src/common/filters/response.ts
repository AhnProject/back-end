import { NextResponse } from "next/server";
import type { ApiResponse } from "@reel-trip/types";
import { AppError } from "../errors/app.error";

export function ok<T>(data: T, message = "success"): NextResponse<ApiResponse<T>> {
  return NextResponse.json({
    success: true,
    data,
    message,
    errorCode: null,
    timestamp: Date.now(),
  });
}

export function handleError(error: unknown): NextResponse<ApiResponse<null>> {
  if (error instanceof AppError) {
    return NextResponse.json(
      {
        success: false,
        data: null,
        message: error.message,
        errorCode: error.errorCode,
        timestamp: Date.now(),
      },
      { status: error.httpStatus }
    );
  }
  console.error(error);
  return NextResponse.json(
    {
      success: false,
      data: null,
      message: "Internal server error",
      errorCode: "INTERNAL_SERVER_ERROR",
      timestamp: Date.now(),
    },
    { status: 500 }
  );
}
