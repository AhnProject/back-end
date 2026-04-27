import { NextRequest } from "next/server";
import { AppError } from "@/app/BE/backend/utils/app-error";

export async function parseJsonBody<T>(request: NextRequest): Promise<T> {
  try {
    return (await request.json()) as T;
  } catch (error) {
    if (error instanceof SyntaxError) {
      throw new AppError("Request body is not valid JSON", "INVALID_JSON", 400);
    }
    throw error;
  }
}

