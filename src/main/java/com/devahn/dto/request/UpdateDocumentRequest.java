package com.devahn.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 문서 수정 요청 DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "문서 수정 요청")
public class UpdateDocumentRequest {

    @Schema(description = "문서 제목", example = "수정된 문서", required = true)
    @NotBlank(message = "제목은 필수입니다")
    @Size(min = 1, max = 255, message = "제목은 1자 이상 255자 이하여야 합니다")
    private String title;

    @Schema(description = "문서 내용", example = "수정된 문서 내용입니다.", required = true)
    @NotBlank(message = "내용은 필수입니다")
    @Size(min = 1, max = 10000, message = "내용은 1자 이상 10000자 이하여야 합니다")
    private String content;

    @Schema(description = "벡터 임베딩 (1536차원)", example = "[0.15, 0.25, 0.35, 0.45, 0.55]", required = false)
    private float[] embedding;
}
