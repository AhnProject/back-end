import { NextRequest, NextResponse } from "next/server";
import { requireAuth } from "@/app/BE/backend/middleware/auth-guard";
import { handleRouteError } from "@/app/BE/backend/middleware/error-handler";
import { ok } from "@/app/BE/backend/utils/api-response";
import { DocumentError } from "@/app/BE/backend/utils/app-error";
import { parseJsonBody } from "@/app/BE/backend/utils/request-json";
import {
  documentInputSchema,
  documentSearchSchema,
} from "@/app/BE/backend/validators/recommend-schema";
import {
  createDocumentWithEmbedding,
  getAllDocuments,
  getDocumentById,
  removeDocumentById,
  searchDocuments,
  updateDocumentById,
} from "@/app/BE/backend/services/document-service";

type ParamsContext = { params: Promise<{ id: string }> };

function parseId(value: string): bigint {
  try {
    return BigInt(value);
  } catch {
    throw DocumentError.invalidInput(`Invalid document id: ${value}`);
  }
}

function serializeDocument(document: Awaited<ReturnType<typeof getDocumentById>>) {
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

export async function createDocument(request: NextRequest): Promise<NextResponse> {
  try {
    await requireAuth(request);
    const body = documentInputSchema.parse(await parseJsonBody(request));
    const id = await createDocumentWithEmbedding(body);
    return NextResponse.json(ok({ id: id.toString() }, "Document created"), { status: 201 });
  } catch (error) {
    return handleRouteError(error);
  }
}

export async function listDocuments(request: NextRequest): Promise<NextResponse> {
  try {
    await requireAuth(request);
    const documents = await getAllDocuments();
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

export async function getDocument(
  request: NextRequest,
  context: ParamsContext
): Promise<NextResponse> {
  try {
    await requireAuth(request);
    const { id } = await context.params;
    const document = await getDocumentById(parseId(id));

    if (!document) {
      throw DocumentError.notFound(id);
    }

    return NextResponse.json(ok(serializeDocument(document), "Document found"));
  } catch (error) {
    return handleRouteError(error);
  }
}

export async function updateDocument(
  request: NextRequest,
  context: ParamsContext
): Promise<NextResponse> {
  try {
    await requireAuth(request);
    const { id } = await context.params;
    const body = documentInputSchema.parse(await parseJsonBody(request));
    const affected = await updateDocumentById(parseId(id), body);

    if (affected === 0) {
      throw DocumentError.notFound(id);
    }

    return NextResponse.json(ok(null, "Document updated"));
  } catch (error) {
    return handleRouteError(error);
  }
}

export async function deleteDocument(
  request: NextRequest,
  context: ParamsContext
): Promise<NextResponse> {
  try {
    await requireAuth(request);
    const { id } = await context.params;
    const affected = await removeDocumentById(parseId(id));

    if (affected === 0) {
      throw DocumentError.notFound(id);
    }

    return NextResponse.json(ok(null, "Document deleted"));
  } catch (error) {
    return handleRouteError(error);
  }
}

export async function searchDocument(request: NextRequest): Promise<NextResponse> {
  try {
    await requireAuth(request);
    const body = documentSearchSchema.parse(await parseJsonBody(request));
    const documents = await searchDocuments(body);

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

export async function healthCheck(): Promise<NextResponse> {
  return NextResponse.json(ok("OK", "Document API is running"));
}

