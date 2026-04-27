package com.devahn.document.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentResponse {

    private Long id;
    private String title;
    private String content;
    private float[] embedding;
    private Float similarity;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
