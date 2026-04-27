import { NextRequest, NextResponse } from "next/server";
import { requireAuth } from "@/lib/middleware/auth-guard";
import { handleRouteError } from "@/lib/middleware/error-handler";
import {
  deleteDocumentById,
  findDocumentById,
  updateDocument as updateDocumentRecord,
} from "@/lib/repositories/document-repo";
import { ok } from "@/lib/utils/api-response";
import { DocumentError } from "@/lib/utils/app-error";
import { documentInputSchema } from "@/lib/validators/recommend-schema";

function parseId(value: string): bigint {
  try {
    return BigInt(value);
  } catch {
    throw DocumentError.invalidInput(`Invalid document id: ${value}`);
  }
}

function serializeDocument(document: Awaited<ReturnType<typeof findDocumentById>>) {
  if (!document) {
    return null;
  }

  return {
    id: document.id.toString(),
    title: document.title,
    content: document.content,
    embedding: document.embedding,
    createdAt: document.createdAt.toISOString(),
    updatedAt: document.updatedAt?.toISOString() ?? null,
  };
}

export async function GET(
  request: NextRequest,
  context: { params: Promise<{ id: string }> }
) {
  try {
    await requireAuth(request);
    const { id } = await context.params;
    const document = await findDocumentById(parseId(id));

    if (!document) {
      throw DocumentError.notFound(id);
    }

    return NextResponse.json(ok(serializeDocument(document), "Document found"));
  } catch (error) {
    return handleRouteError(error);
  }
}

export async function PUT(
  request: NextRequest,
  context: { params: Promise<{ id: string }> }
) {
  try {
    await requireAuth(request);
    const { id } = await context.params;
    const body = documentInputSchema.parse(await request.json());
    const affected = await updateDocumentRecord(parseId(id), body);

    if (affected === 0) {
      throw DocumentError.notFound(id);
    }

    return NextResponse.json(ok(null, "Document updated"));
  } catch (error) {
    return handleRouteError(error);
  }
}

export async function DELETE(
  request: NextRequest,
  context: { params: Promise<{ id: string }> }
) {
  try {
    await requireAuth(request);
    const { id } = await context.params;
    const affected = await deleteDocumentById(parseId(id));

    if (affected === 0) {
      throw DocumentError.notFound(id);
    }

    return NextResponse.json(ok(null, "Document deleted"));
  } catch (error) {
    return handleRouteError(error);
  }
}
