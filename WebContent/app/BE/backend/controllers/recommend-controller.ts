import { NextRequest, NextResponse } from "next/server";
import { handleRouteError } from "@/app/BE/backend/middleware/error-handler";
import { ok } from "@/app/BE/backend/utils/api-response";
import { parseJsonBody } from "@/app/BE/backend/utils/request-json";
import { recommendRequestSchema } from "@/app/BE/backend/validators/recommend-schema";
import { recommend } from "@/app/BE/backend/services/recommend-service";

export async function postRecommend(request: NextRequest): Promise<NextResponse> {
  try {
    const body = recommendRequestSchema.parse(await parseJsonBody(request));
    const result = await recommend(body);

    return NextResponse.json(
      ok(
        {
          originalQuery: result.originalQuery,
          refinedQuery: result.refinedQuery,
          keywords: result.keywords,
          results: result.results.map((document) => ({
            id: document.id.toString(),
            title: document.title,
            content: document.content,
            similarity: document.similarity,
            createdAt: document.createdAt.toISOString(),
          })),
          totalCount: result.totalCount,
        },
        "Recommend succeeded"
      )
    );
  } catch (error) {
    return handleRouteError(error);
  }
}

export async function getRecommendHealth(): Promise<NextResponse> {
  return NextResponse.json(
    ok(
      {
        endpoint: "POST /api/recommend",
        description: "Natural language recommendation endpoint backed by OpenAI and pgvector.",
      },
      "Recommend API is running"
    )
  );
}

