package com.devahn.support.util;

import com.devahn.support.exception.DocumentException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VectorUtils {

    public static String vectorToString(float[] embedding) {
        if (embedding == null) {
            throw new DocumentException("Vector must not be null", "NULL_VECTOR", 400);
        }

        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < embedding.length; i++) {
            sb.append(embedding[i]);
            if (i < embedding.length - 1) {
                sb.append(",");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    public static float[] stringToVector(String embeddingStr, int expectedDim) {
        if (embeddingStr == null || embeddingStr.trim().isEmpty()) {
            return new float[expectedDim];
        }

        try {
            String cleaned = embeddingStr.replace("[", "").replace("]", "").trim();
            if (cleaned.isEmpty()) {
                return new float[expectedDim];
            }

            String[] parts = cleaned.split(",");
            float[] embedding = new float[expectedDim];

            for (int i = 0; i < Math.min(parts.length, expectedDim); i++) {
                embedding[i] = Float.parseFloat(parts[i].trim());
            }
            return embedding;
        } catch (NumberFormatException e) {
            throw DocumentException.invalidInput("Failed to parse vector: " + embeddingStr);
        }
    }

    public static float[] adjustVectorDimension(float[] embedding, int targetDim) {
        if (embedding == null) {
            return new float[targetDim];
        }

        if (embedding.length == targetDim) {
            return embedding;
        }

        float[] adjusted = new float[targetDim];
        for (int i = 0; i < Math.min(embedding.length, targetDim); i++) {
            adjusted[i] = embedding[i];
        }
        return adjusted;
    }
}
