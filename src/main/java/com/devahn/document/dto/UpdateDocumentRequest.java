package com.devahn.document.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Update document request")
public class UpdateDocumentRequest {

    @Schema(description = "Document title", example = "Updated document", required = true)
    @NotBlank(message = "Title is required")
    @Size(min = 1, max = 255, message = "Title must be between 1 and 255 characters")
    private String title;

    @Schema(description = "Document content", example = "Updated document content.", required = true)
    @NotBlank(message = "Content is required")
    @Size(min = 1, max = 10000, message = "Content must be between 1 and 10000 characters")
    private String content;

    @Schema(description = "Embedding vector", example = "[0.15, 0.25, 0.35, 0.45, 0.55]", required = false)
    private float[] embedding;
}
