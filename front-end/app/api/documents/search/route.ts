import { NextRequest, NextResponse } from "next/server";
import { requireAuth } from "@/lib/middleware/auth-guard";
import { handleRouteError } from "@/lib/middleware/error-handler";
import { searchDocumentsByVector } from "@/lib/repositories/document-repo";
import { ok } from "@/lib/utils/api-response";
import { documentSearchSchema } from "@/lib/validators/recommend-schema";

export async function POST(request: NextRequest) {
  try {
    await requireAuth(request);
    const body = documentSearchSchema.parse(await request.json());
    const documents = await searchDocumentsByVector(
      body.embedding,
      body.limit,
      body.threshold
    );

    return NextResponse.json(
      ok(
        documents.map((document) => ({
          id: document.id.toString(),
          title: document.title,
          content: document.content,
          embedding: document.embedding,
          similarity: document.similarity,
          createdAt: document.createdAt.toISOString(),
          updatedAt: document.updatedAt?.toISOString() ?? null,
        })),
        `Found ${documents.length} similar document(s)`
      )
    );
  } catch (error) {
    return handleRouteError(error);
  }
}
