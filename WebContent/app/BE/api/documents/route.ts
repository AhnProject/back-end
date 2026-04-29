import {
  createDocument,
  listDocuments,
} from "@/app/BE/backend/controllers/document-controller";

export const POST = createDocument;
export const GET = listDocuments;


