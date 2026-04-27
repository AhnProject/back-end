import { NextRequest, NextResponse } from "next/server";
import { login } from "@/lib/services/auth-service";
import { handleRouteError } from "@/lib/middleware/error-handler";
import { ok } from "@/lib/utils/api-response";
import { loginRequestSchema } from "@/lib/validators/recommend-schema";

export async function POST(request: NextRequest) {
  try {
    const body = loginRequestSchema.parse(await request.json());
    const result = await login(body);

    return NextResponse.json(ok(result, "Login succeeded"));
  } catch (error) {
    return handleRouteError(error);
  }
}
