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
@Schema(description = "Create document request")
public class CreateDocumentRequest {

    @Schema(description = "Document title", example = "Sample document", required = true)
    @NotBlank(message = "Title is required")
    @Size(min = 1, max = 255, message = "Title must be between 1 and 255 characters")
    private String title;

    @Schema(description = "Document content", example = "This is a sample document.", required = true)
    @NotBlank(message = "Content is required")
    @Size(min = 1, max = 10000, message = "Content must be between 1 and 10000 characters")
    private String content;

    @Schema(description = "Embedding vector", example = "[0.1, 0.2, 0.3, 0.4, 0.5]", required = false)
    private float[] embedding;
}
