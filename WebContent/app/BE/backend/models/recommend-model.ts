import type { SimilarDocumentRecord } from "@/app/BE/backend/models/document-model";

export interface ParsedQuery {
  originalText: string;
  keywords: string[];
  refinedQuery: string;
  embedding: number[];
}

export interface RecommendInput {
  query: string;
  topK: number;
  threshold: number;
}

export interface RecommendOutput {
  originalQuery: string;
  refinedQuery: string;
  keywords: string[];
  results: SimilarDocumentRecord[];
  totalCount: number;
}


