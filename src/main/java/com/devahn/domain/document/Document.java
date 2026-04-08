package com.devahn.domain.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * 문서 엔티티
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Document {
    private Long id;
    private String title;
    private String content;
    private float[] embedding;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}