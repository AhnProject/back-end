package com.devahn.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 문서 응답 DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentResponse {

    private Long id;

    private String title;

    private String content;

    private float[] embedding;

    private Float similarity; // 검색 시 유사도 점수

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @Override
    public String toString() {
        return "DocumentResponse{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + (content != null && content.length() > 50 
                    ? content.substring(0, 50) + "..." 
                    : content) + '\'' +
                ", embeddingLength=" + (embedding != null ? embedding.length : 0) +
                ", similarity=" + similarity +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
