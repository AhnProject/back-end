import { NextRequest, NextResponse } from "next/server";
import { parseAndEmbed } from "@/lib/services/ai-service";
import { findSimilarDocuments } from "@/lib/repositories/recommend-repo";
import { handleRouteError } from "@/lib/middleware/error-handler";
import { ok } from "@/lib/utils/api-response";
import { recommendRequestSchema } from "@/lib/validators/recommend-schema";

export async function POST(request: NextRequest) {
  try {
    const body = recommendRequestSchema.parse(await request.json());
    const parsedQuery = await parseAndEmbed(body.query);
    const results = await findSimilarDocuments(
      parsedQuery.embedding,
      body.topK,
      body.threshold
    );

    return NextResponse.json(
      ok(
        {
          originalQuery: parsedQuery.originalText,
          refinedQuery: parsedQuery.refinedQuery,
          keywords: parsedQuery.keywords,
          results: results.map((document) => ({
            id: document.id.toString(),
            title: document.title,
            content: document.content,
            similarity: document.similarity,
            createdAt: document.createdAt.toISOString(),
          })),
          totalCount: results.length,
        },
        "Recommend succeeded"
      )
    );
  } catch (error) {
    return handleRouteError(error);
  }
}

export async function GET() {
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
