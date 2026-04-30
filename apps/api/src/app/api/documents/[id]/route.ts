import { type NextRequest } from "next/server";
import { ZodError } from "zod";
import { DocumentService } from "@/document/document.service";
import { UpdateDocumentSchema } from "@/document/dto/document.dto";
import { ok, handleError } from "@/common/filters/response";
import { verifyJwt } from "@/common/guards/jwt-auth";
import { AppError } from "@/common/errors/app.error";

const documentService = new DocumentService();

export async function GET(
  request: NextRequest,
  { params }: { params: Promise<{ id: string }> }
) {
  try {
    await verifyJwt(request);
    const { id } = await params;
    const data = await documentService.findById(id);
    return ok(data);
  } catch (error) {
    return handleError(error);
  }
}

export async function PUT(
  request: NextRequest,
  { params }: { params: Promise<{ id: string }> }
) {
  try {
    await verifyJwt(request);
    const { id } = await params;
    const body = await request.json();
    const dto = UpdateDocumentSchema.parse(body);
    await documentService.update(id, dto);
    return ok(null, "Document updated");
  } catch (error) {
    if (error instanceof ZodError) {
      return handleError(new AppError(error.errors[0]!.message, "VALIDATION_ERROR", 400));
    }
    return handleError(error);
  }
}

export async function DELETE(
  request: NextRequest,
  { params }: { params: Promise<{ id: string }> }
) {
  try {
    await verifyJwt(request);
    const { id } = await params;
    await documentService.delete(id);
    return ok(null, "Document deleted");
  } catch (error) {
    return handleError(error);
  }
}
