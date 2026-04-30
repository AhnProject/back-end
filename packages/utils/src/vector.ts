export const VECTOR_DIM = 1536;

export function vectorToString(embedding: number[]): string {
  if (!embedding || embedding.length === 0) {
    throw new Error("Vector must not be null or empty");
  }
  return `[${embedding.join(",")}]`;
}

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

export function adjustVectorDimension(
  embedding: number[] | null,
  targetDim = VECTOR_DIM
): number[] {
  if (!embedding) return new Array(targetDim).fill(0);
  if (embedding.length === targetDim) return embedding;
  const adjusted = new Array(targetDim).fill(0);
  for (let i = 0; i < Math.min(embedding.length, targetDim); i++) {
    adjusted[i] = embedding[i];
  }
  return adjusted;
}
