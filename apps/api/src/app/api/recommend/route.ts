import { type NextRequest } from "next/server";
import { ZodError } from "zod";
import { RecommendService } from "@/recommend/recommend.service";
import { RecommendSchema } from "@/recommend/dto/recommend.dto";
import { ok, handleError } from "@/common/filters/response";
import { AppError } from "@/common/errors/app.error";

const recommendService = new RecommendService();

export async function GET() {
  return ok(
    { endpoint: "/api/recommend", description: "AI-powered travel recommendation" },
    "Recommend API is healthy"
  );
}

export async function POST(request: NextRequest) {
  try {
    const body = await request.json();
    const dto = RecommendSchema.parse(body);
    const data = await recommendService.recommend(dto);
    return ok(data);
  } catch (error) {
    if (error instanceof ZodError) {
      return handleError(new AppError(error.errors[0]!.message, "VALIDATION_ERROR", 400));
    }
    return handleError(error);
  }
}
