package com.devahn.controller;

import com.devahn.domain.document.Document;
import org.springframework.web.bind.annotation.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.dao.DataAccessException;

import java.util.List;
import java.util.Arrays;

@RestController
@RequestMapping("/api/document")
public class DocumentController {

    private final JdbcTemplate jdbcTemplate;

    private static final int VECTOR_DIM = 1536; // pgvector 컬럼 차원

    public DocumentController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // ---------------------------
    // Document Insert
    // ---------------------------
    @PostMapping("/insert")
    public String insert(@RequestBody Document doc) {
        try {
            float[] embedding = doc.getEmbedding();

            // ---------------------------
            // 벡터 길이 자동 조정
            // ---------------------------
            if (embedding == null) {
                embedding = new float[VECTOR_DIM]; // 모두 0.0
            } else if (embedding.length != VECTOR_DIM) {
                float[] newEmbedding = new float[VECTOR_DIM];
                for (int i = 0; i < Math.min(embedding.length, VECTOR_DIM); i++) {
                    newEmbedding[i] = embedding[i];
                }
                embedding = newEmbedding;
            }

            // ---------------------------
            // 벡터 문자열 변환: [x,x,x,...]
            // ---------------------------
            StringBuilder sb = new StringBuilder();
            sb.append("[");
            for (int i = 0; i < embedding.length; i++) {
                sb.append(embedding[i]);
                if (i < embedding.length - 1) sb.append(",");
            }
            sb.append("]");
            String vectorString = sb.toString();

            // ---------------------------
            // Insert SQL 실행
            // ---------------------------
            String sql = "INSERT INTO documents(title, content, embedding, created_at) " +
                    "VALUES (?, ?, ?::vector, now())";
            jdbcTemplate.update(sql, doc.getTitle(), doc.getContent(), vectorString);

            return "삽입 완료: " + doc.getTitle();
        } catch (DataAccessException e) {
            e.printStackTrace();
            return "삽입 실패: " + e.getMessage();
        }
    }

    // ---------------------------
    // Document Select All
    // ---------------------------
    @GetMapping("/all")
    public List<Document> getAll() {
        String sql = "SELECT id, title, content, embedding, created_at FROM documents";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Document d = new Document();
            d.setId(rs.getLong("id"));
            d.setTitle(rs.getString("title"));
            d.setContent(rs.getString("content"));

            // ---------------------------
            // vector 문자열 → float[]
            // ---------------------------
            String embeddingStr = rs.getString("embedding").replace("[","").replace("]","");
            String[] parts = embeddingStr.split(",");
            float[] embedding = new float[VECTOR_DIM];
            for (int i = 0; i < Math.min(parts.length, VECTOR_DIM); i++) {
                embedding[i] = Float.parseFloat(parts[i]);
            }
            d.setEmbedding(embedding);

            d.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
            return d;
        });
    }
}