export interface DocumentRecord {
  id: string;
  title: string;
  content: string;
  embedding: number[] | null;
  createdAt: string;
  updatedAt: string | null;
}

export interface CreateDocumentInput {
  title: string;
  content: string;
  embedding?: number[] | null;
}

export interface UpdateDocumentInput {
  title: string;
  content: string;
  embedding?: number[] | null;
}

export interface SearchDocumentInput {
  embedding: number[];
  limit: number;
  threshold?: number;
}

export interface SimilarDocumentRecord extends DocumentRecord {
  similarity: number;
}
