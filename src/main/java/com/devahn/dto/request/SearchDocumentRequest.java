package com.devahn.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "벡터 유사도 검색 요청")
public class SearchDocumentRequest {

    @Schema(description = "검색할 벡터 임베딩 (1536차원)", example = "[0.2, 0.3, 0.4, 0.5, 0.6]", required = true)
    @NotNull(message = "검색 벡터는 필수입니다")
    private float[] embedding;

    @Schema(description = "검색 결과 최대 개수", example = "10", defaultValue = "10")
    @Builder.Default
    @Min(value = 1, message = "결과 개수는 최소 1개 이상이어야 합니다")
    private int limit = 10;

    @Schema(description = "유사도 임계값 (0.0 ~ 1.0)", example = "0.7", defaultValue = "0.7")
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
