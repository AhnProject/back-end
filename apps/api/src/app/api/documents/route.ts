import { type NextRequest } from "next/server";
import { ZodError } from "zod";
import { DocumentService } from "@/document/document.service";
import { CreateDocumentSchema } from "@/document/dto/document.dto";
import { ok, handleError } from "@/common/filters/response";
import { verifyJwt } from "@/common/guards/jwt-auth";
import { AppError } from "@/common/errors/app.error";

const documentService = new DocumentService();

export async function GET(request: NextRequest) {
  try {
    await verifyJwt(request);
    const data = await documentService.findAll();
    return ok(data);
  } catch (error) {
    return handleError(error);
  }
}

export async function POST(request: NextRequest) {
  try {
    await verifyJwt(request);
    const body = await request.json();
    const dto = CreateDocumentSchema.parse(body);
    const data = await documentService.create(dto);
    return ok(data);
  } catch (error) {
    if (error instanceof ZodError) {
      return handleError(new AppError(error.errors[0]!.message, "VALIDATION_ERROR", 400));
    }
    return handleError(error);
  }
}
