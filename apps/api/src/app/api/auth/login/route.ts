import { type NextRequest } from "next/server";
import { ZodError } from "zod";
import { AuthService } from "@/auth/auth.service";
import { LoginSchema } from "@/auth/dto/auth.dto";
import { ok, handleError } from "@/common/filters/response";
import { AppError } from "@/common/errors/app.error";

const authService = new AuthService();

export async function POST(request: NextRequest) {
  try {
    const body = await request.json();
    const dto = LoginSchema.parse(body);
    const data = await authService.login(dto);
    return ok(data);
  } catch (error) {
    if (error instanceof ZodError) {
      return handleError(new AppError(error.errors[0]!.message, "VALIDATION_ERROR", 400));
    }
    return handleError(error);
  }
}
