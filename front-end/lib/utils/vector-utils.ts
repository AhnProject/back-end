// ============================================================
// VectorUtils — Spring Boot VectorUtils.java 완전 이식
// 벡터 직렬화 / 역직렬화 / 차원 보정 유틸
// ============================================================

export const VECTOR_DIM = 1536;

/**
 * float[] → "[0.1,0.2,...]" (PostgreSQL pgvector 입력 형식)
 * Java: VectorUtils.vectorToString(float[])
 */
export function vectorToString(embedding: number[]): string {
  if (!embedding || embedding.length === 0) {
    throw new Error("Vector must not be null or empty");
  }
  return `[${embedding.join(",")}]`;
}

/**
 * "[0.1,0.2,...]" → number[] (DB 조회 후 역직렬화)
 * Java: VectorUtils.stringToVector(String, int)
 */
export function stringToVector(embeddingStr: string | null, expectedDim = VECTOR_DIM): number[] {
  if (!embeddingStr || embeddingStr.trim() === "") {
    return new Array(expectedDim).fill(0);
  }
  try {
    const cleaned = embeddingStr.replace("[", "").replace("]", "").trim();
    if (!cleaned) return new Array(expectedDim).fill(0);
    const parts = cleaned.split(",");
    const result = new Array(expectedDim).fill(0);
    for (let i = 0; i < Math.min(parts.length, expectedDim); i++) {
      result[i] = parseFloat(parts[i].trim());
    }
    return result;
  } catch {
    throw new Error(`Failed to parse vector: ${embeddingStr}`);
  }
}

/**
 * 벡터 차원 보정 (부족하면 0 패딩, 초과하면 잘라냄)
 * Java: VectorUtils.adjustVectorDimension(float[], int)
 */
export function adjustVectorDimension(embedding: number[] | null, targetDim = VECTOR_DIM): number[] {
  if (!embedding) return new Array(targetDim).fill(0);
  if (embedding.length === targetDim) return embedding;
  const adjusted = new Array(targetDim).fill(0);
  for (let i = 0; i < Math.min(embedding.length, targetDim); i++) {
    adjusted[i] = embedding[i];
  }
  return adjusted;
}
