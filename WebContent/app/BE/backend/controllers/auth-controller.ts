import { NextRequest, NextResponse } from "next/server";
import { handleRouteError } from "@/app/BE/backend/middleware/error-handler";
import { ok } from "@/app/BE/backend/utils/api-response";
import { parseJsonBody } from "@/app/BE/backend/utils/request-json";
import {
  loginRequestSchema,
  signupRequestSchema,
} from "@/app/BE/backend/validators/recommend-schema";
import { login, signup } from "@/app/BE/backend/services/auth-service";

export async function postSignup(request: NextRequest): Promise<NextResponse> {
  try {
    const body = signupRequestSchema.parse(await parseJsonBody(request));
    const result = await signup(body);
    return NextResponse.json(ok(result, "Signup completed"), { status: 201 });
  } catch (error) {
    return handleRouteError(error);
  }
}

export async function postLogin(request: NextRequest): Promise<NextResponse> {
  try {
    const body = loginRequestSchema.parse(await parseJsonBody(request));
    const result = await login(body);
    return NextResponse.json(ok(result, "Login succeeded"));
  } catch (error) {
    return handleRouteError(error);
  }
}

