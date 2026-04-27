import type {
  RecommendInput,
  RecommendOutput,
} from "@/app/BE/backend/models/recommend-model";
import { findSimilarDocuments } from "@/app/BE/backend/repositories/recommend-repository";
import { parseAndEmbed } from "@/app/BE/backend/services/ai-service";

export async function recommend(input: RecommendInput): Promise<RecommendOutput> {
  const parsedQuery = await parseAndEmbed(input.query);
  const results = await findSimilarDocuments(
    parsedQuery.embedding,
    input.topK,
    input.threshold
  );

  return {
    originalQuery: parsedQuery.originalText,
    refinedQuery: parsedQuery.refinedQuery,
    keywords: parsedQuery.keywords,
    results,
    totalCount: results.length,
  };
}


