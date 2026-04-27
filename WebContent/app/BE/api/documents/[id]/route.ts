import {
  deleteDocument,
  getDocument,
  updateDocument,
} from "@/app/BE/backend/controllers/document-controller";

export const GET = getDocument;
export const PUT = updateDocument;
export const DELETE = deleteDocument;


