import type { SimilarDocumentRecord } from "./document.types";

export interface RecommendInput {
  query: string;
  topK: number;
  threshold: number;
}

export interface RecommendResult {
  id: string;
  title: string;
  content: string;
  similarity: number;
  createdAt: string;
}

export interface RecommendOutput {
  originalQuery: string;
  refinedQuery: string;
  keywords: string[];
  results: SimilarDocumentRecord[];
  totalCount: number;
}
