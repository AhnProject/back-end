import { NextRequest, NextResponse } from "next/server";
import { createEmbedding } from "@/lib/services/ai-service";
import { requireAuth } from "@/lib/middleware/auth-guard";
import { handleRouteError } from "@/lib/middleware/error-handler";
import { findAllDocuments, saveDocument } from "@/lib/repositories/document-repo";
import { ok } from "@/lib/utils/api-response";
import { documentInputSchema } from "@/lib/validators/recommend-schema";

export async function POST(request: NextRequest) {
  try {
    await requireAuth(request);
    const body = documentInputSchema.parse(await request.json());
    const embedding =
      body.embedding && body.embedding.length > 0
        ? body.embedding
        : await createEmbedding(`${body.title} ${body.content}`);

    const documentId = await saveDocument({
      title: body.title,
      content: body.content,
      embedding,
    });

    return NextResponse.json(ok({ id: documentId.toString() }, "Document created"), {
      status: 201,
    });
  } catch (error) {
    return handleRouteError(error);
  }
}

export async function GET(request: NextRequest) {
  try {
    await requireAuth(request);
    const documents = await findAllDocuments();

    return NextResponse.json(
      ok(
        documents.map((document) => ({
          id: document.id.toString(),
          title: document.title,
          content: document.content,
          embedding: document.embedding,
          createdAt: document.createdAt.toISOString(),
          updatedAt: document.updatedAt?.toISOString() ?? null,
        })),
        `Returned ${documents.length} document(s)`
      )
    );
  } catch (error) {
    return handleRouteError(error);
  }
}
