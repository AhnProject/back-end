import { NextRequest, NextResponse } from "next/server";
import { signup } from "@/lib/services/auth-service";
import { handleRouteError } from "@/lib/middleware/error-handler";
import { ok } from "@/lib/utils/api-response";
import { signupRequestSchema } from "@/lib/validators/recommend-schema";

export async function POST(request: NextRequest) {
  try {
    const body = signupRequestSchema.parse(await request.json());
    const result = await signup(body);

    return NextResponse.json(ok(result, "Signup completed"), { status: 201 });
  } catch (error) {
    return handleRouteError(error);
  }
}
