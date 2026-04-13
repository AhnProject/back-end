package com.devahn.document.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Vector search request")
public class SearchDocumentRequest {

    @Schema(description = "Query embedding vector", example = "[0.2, 0.3, 0.4, 0.5, 0.6]", required = true)
    @NotNull(message = "Embedding is required")
    private float[] embedding;

    @Schema(description = "Maximum result count", example = "10", defaultValue = "10")
    @Builder.Default
    @Min(value = 1, message = "Limit must be at least 1")
    private int limit = 10;

    @Schema(description = "Similarity threshold hint", example = "0.7", defaultValue = "0.7")
    @Builder.Default
    private float threshold = 0.7f;
}
