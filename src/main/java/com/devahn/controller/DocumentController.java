package com.devahn.controller;

import com.devahn.domain.document.Document;
import com.devahn.util.VectorUtils;
import com.devahn.common.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.dao.DataAccessException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/document")
public class DocumentController {

    private final JdbcTemplate jdbcTemplate;
    private static final int VECTOR_DIM = 1536;

    public DocumentController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * 문서 삽입
     */
    @PostMapping("/insert")
    public ApiResponse<?> insert(@RequestBody Document doc) {
        try {
            // 입력 검증
            if (doc.getTitle() == null || doc.getTitle().trim().isEmpty()) {
                throw new IllegalArgumentException("제목은 필수입니다");
            }
            if (doc.getContent() == null || doc.getContent().trim().isEmpty()) {
                throw new IllegalArgumentException("내용은 필수입니다");
            }

            // 벡터 차원 조정
            float[] embedding = VectorUtils.adjustVectorDimension(doc.getEmbedding(), VECTOR_DIM);

            // 벡터 → 문자열 변환
            String vectorString = VectorUtils.vectorToString(embedding);

            String sql = "INSERT INTO documents(title, content, embedding, created_at) " +
                    "VALUES (?, ?, ?::vector, now())";
            jdbcTemplate.update(sql, doc.getTitle(), doc.getContent(), vectorString);

            log.info("문서 삽입 성공: {}", doc.getTitle());
            return ApiResponse.ok("삽입 완료: " + doc.getTitle());
        } catch (IllegalArgumentException e) {
            log.warn("입력 검증 실패: {}", e.getMessage());
            return ApiResponse.error("INVALID_INPUT", e.getMessage());
        } catch (DataAccessException e) {
            log.error("DB 접근 오류: {}", e.getMessage(), e);
            return ApiResponse.error("DB_ERROR", "데이터베이스 오류가 발생했습니다");
        } catch (Exception e) {
            log.error("예상치 못한 오류: {}", e.getMessage(), e);
            return ApiResponse.error("INTERNAL_ERROR", "서버 오류가 발생했습니다");
        }
    }

    /**
     * 전체 문서 조회
     */
    @GetMapping("/all")
    public ApiResponse<?> getAll() {
        try {
            String sql = "SELECT id, title, content, embedding, created_at FROM documents";
            List<Document> documents = jdbcTemplate.query(sql, (rs, rowNum) -> {
                String embeddingStr = rs.getString("embedding");

                // null 체크
                if (embeddingStr == null || embeddingStr.trim().isEmpty()) {
                    embeddingStr = "[" + "0.0,".repeat(VECTOR_DIM - 1) + "0.0]";
                }

                float[] embedding = VectorUtils.stringToVector(embeddingStr, VECTOR_DIM);

                return Document.builder()
                        .id(rs.getLong("id"))
                        .title(rs.getString("title"))
                        .content(rs.getString("content"))
                        .embedding(embedding)
                        .createdAt(rs.getTimestamp("created_at") != null
                                ? rs.getTimestamp("created_at").toLocalDateTime()
                                : null)
                        .build();
            });

            log.info("문서 조회 성공: {} 건", documents.size());
            return ApiResponse.ok(documents, "조회 완료: " + documents.size() + "건");
        } catch (DataAccessException e) {
            log.error("DB 접근 오류: {}", e.getMessage(), e);
            return ApiResponse.error("DB_ERROR", "데이터베이스 오류가 발생했습니다");
        } catch (Exception e) {
            log.error("예상치 못한 오류: {}", e.getMessage(), e);
            return ApiResponse.error("INTERNAL_ERROR", "서버 오류가 발생했습니다");
        }
    }
}