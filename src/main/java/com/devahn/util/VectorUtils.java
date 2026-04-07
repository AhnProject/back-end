package com.devahn.util;

import com.devahn.exception.DocumentException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * 벡터 처리 유틸리티
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VectorUtils {

    /**
     * 벡터 배열을 문자열로 변환 (예: [0.1,0.2,0.3,...])
     */
    public static String vectorToString(float[] embedding) {
        if (embedding == null) {
            throw new DocumentException("벡터가 null입니다", "NULL_VECTOR", 400);
        }

        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < embedding.length; i++) {
            sb.append(embedding[i]);
            if (i < embedding.length - 1) sb.append(",");
        }
        sb.append("]");
        return sb.toString();
    }

    /**
     * 문자열을 벡터 배열로 변환
     */
    public static float[] stringToVector(String embeddingStr, int expectedDim) {
        if (embeddingStr == null || embeddingStr.trim().isEmpty()) {
            return new float[expectedDim]; // 0으로 초기화
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
            throw DocumentException.invalidInput("벡터 문자열 파싱 실패: " + embeddingStr);
        }
    }

    /**
     * 벡터 차원 자동 조정
     */
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

    /**
     * 코사인 유사도 계산
     */
    public static float cosineSimilarity(float[] a, float[] b) {
        if (a.length != b.length) {
            throw DocumentException.invalidVectorDimension(a.length, b.length);
        }

        float dotProduct = 0.0f;
        float normA = 0.0f;
        float normB = 0.0f;

        for (int i = 0; i < a.length; i++) {
            dotProduct += a[i] * b[i];
            normA += a[i] * a[i];
            normB += b[i] * b[i];
        }

        normA = (float) Math.sqrt(normA);
        normB = (float) Math.sqrt(normB);

        if (normA == 0.0f || normB == 0.0f) {
            return 0.0f;
        }

        return dotProduct / (normA * normB);
    }

    /**
     * 벡터 정규화
     */
    public static float[] normalize(float[] vector) {
        float norm = 0.0f;
        for (float v : vector) {
            norm += v * v;
        }
        norm = (float) Math.sqrt(norm);

        if (norm == 0.0f) {
            return vector;
        }

        float[] normalized = new float[vector.length];
        for (int i = 0; i < vector.length; i++) {
            normalized[i] = vector[i] / norm;
        }
        return normalized;
    }

    /**
     * 벡터 거리 계산 (유클리드 거리)
     */
    public static float euclideanDistance(float[] a, float[] b) {
        if (a.length != b.length) {
            throw DocumentException.invalidVectorDimension(a.length, b.length);
        }

        float sum = 0.0f;
        for (int i = 0; i < a.length; i++) {
            float diff = a[i] - b[i];
            sum += diff * diff;
        }
        return (float) Math.sqrt(sum);
    }
}
