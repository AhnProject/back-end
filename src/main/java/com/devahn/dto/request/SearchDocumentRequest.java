package com.devahn.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 벡터 유사도 검색 요청 DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchDocumentRequest {

    @NotNull(message = "검색 벡터는 필수입니다")
    private float[] embedding;

    @Builder.Default
    @Min(value = 1, message = "결과 개수는 최소 1개 이상이어야 합니다")
    private int limit = 10;

    @Builder.Default
    private float threshold = 0.7f;

    @Override
    public String toString() {
        return "SearchDocumentRequest{" +
                "embeddingLength=" + (embedding != null ? embedding.length : 0) +
                ", limit=" + limit +
                ", threshold=" + threshold +
                '}';
    }
}
