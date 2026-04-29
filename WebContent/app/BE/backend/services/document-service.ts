import type {
  CreateDocumentInput,
  SearchDocumentInput,
  UpdateDocumentInput,
} from "@/app/BE/backend/models/document-model";
import {
  createDocument,
  deleteDocumentById,
  findAllDocuments,
  findDocumentById,
  searchDocumentsByVector,
  updateDocument,
} from "@/app/BE/backend/repositories/document-repository";
import { createEmbedding } from "@/app/BE/backend/services/ai-service";

export async function createDocumentWithEmbedding(input: CreateDocumentInput): Promise<bigint> {
  const embedding =
    input.embedding && input.embedding.length > 0
      ? input.embedding
      : await createEmbedding(`${input.title} ${input.content}`);

  return createDocument({
    title: input.title,
    content: input.content,
    embedding,
  });
}

export async function getAllDocuments() {
  return findAllDocuments();
}

export async function getDocumentById(id: bigint) {
  return findDocumentById(id);
}

export async function updateDocumentById(id: bigint, input: UpdateDocumentInput) {
  return updateDocument(id, input);
}

export async function removeDocumentById(id: bigint) {
  return deleteDocumentById(id);
}

export async function searchDocuments(input: SearchDocumentInput) {
  return searchDocumentsByVector(input.embedding, input.limit, input.threshold);
}


