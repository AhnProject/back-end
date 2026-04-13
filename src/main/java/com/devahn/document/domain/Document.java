package com.devahn.document.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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
