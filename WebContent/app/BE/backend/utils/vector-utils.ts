// ============================================================
// VectorUtils ??Spring Boot VectorUtils.java ?꾩쟾 ?댁떇
// 踰≫꽣 吏곷젹??/ ??쭅?ы솕 / 李⑥썝 蹂댁젙 ?좏떥
// ============================================================

function parseVectorDimension(): number {
  const raw = process.env.VECTOR_DIMENSION ?? "1536";
  const parsed = Number.parseInt(raw, 10);

  if (!Number.isInteger(parsed) || parsed <= 0) {
    throw new Error("VECTOR_DIMENSION must be a positive integer");
  }

  return parsed;
}

export const VECTOR_DIM = parseVectorDimension();

/**
 * float[] ??"[0.1,0.2,...]" (PostgreSQL pgvector ?낅젰 ?뺤떇)
 * Java: VectorUtils.vectorToString(float[])
 */
export function vectorToString(embedding: number[]): string {
  if (!embedding || embedding.length === 0) {
    throw new Error("Vector must not be null or empty");
  }
  return `[${embedding.join(",")}]`;
}

/**
 * "[0.1,0.2,...]" ??number[] (DB 議고쉶 ????쭅?ы솕)
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
 * 踰≫꽣 李⑥썝 蹂댁젙 (遺議깊븯硫?0 ?⑤뵫, 珥덇낵?섎㈃ ?섎씪??
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
