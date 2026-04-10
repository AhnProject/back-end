package com.devahn.repository;

import com.devahn.domain.document.Document;
import com.devahn.util.VectorUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

/**
 * Document Repository 구현체
 * JDBC Template을 사용한 데이터 액세스
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class DocumentRepositoryImpl implements DocumentRepository {

    private final JdbcTemplate jdbcTemplate;
    private static final int VECTOR_DIM = 1536;

    /**
     * Document RowMapper
     */
    private final RowMapper<Document> documentRowMapper = (rs, rowNum) -> {
        String embeddingStr = rs.getString("embedding");
        
        // null 체크 및 기본값 설정
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
                .updatedAt(rs.getTimestamp("updated_at") != null 
                        ? rs.getTimestamp("updated_at").toLocalDateTime() 
                        : null)
                .build();
    };

    @Override
    public Long save(Document document) {
        String sql = "INSERT INTO documents(title, content, embedding, created_at) " +
                "VALUES (?, ?, ?::vector, now())";
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        
        // 벡터 차원 조정 및 문자열 변환
        float[] embedding = VectorUtils.adjustVectorDimension(document.getEmbedding(), VECTOR_DIM);
        String vectorString = VectorUtils.vectorToString(embedding);
        
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, document.getTitle());
            ps.setString(2, document.getContent());
            ps.setString(3, vectorString);
            return ps;
        }, keyHolder);
        
        Number key = keyHolder.getKey();
        return key != null ? key.longValue() : null;
    }

    @Override
    public Optional<Document> findById(Long id) {
        String sql = "SELECT id, title, content, embedding, created_at, updated_at " +
                "FROM documents WHERE id = ?";
        
        List<Document> results = jdbcTemplate.query(sql, documentRowMapper, id);
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    @Override
    public List<Document> findAll() {
        String sql = "SELECT id, title, content, embedding, created_at, updated_at " +
                "FROM documents ORDER BY created_at DESC";
        
        return jdbcTemplate.query(sql, documentRowMapper);
    }

    @Override
    public int update(Document document) {
        String sql = "UPDATE documents SET title = ?, content = ?, embedding = ?::vector, " +
                "updated_at = now() WHERE id = ?";
        
        // 벡터 차원 조정 및 문자열 변환
        float[] embedding = VectorUtils.adjustVectorDimension(document.getEmbedding(), VECTOR_DIM);
        String vectorString = VectorUtils.vectorToString(embedding);
        
        return jdbcTemplate.update(sql, 
                document.getTitle(), 
                document.getContent(), 
                vectorString,
                document.getId());
    }

    @Override
    public int deleteById(Long id) {
        String sql = "DELETE FROM documents WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }

    @Override
    public List<Document> searchByVector(float[] queryVector, int limit) {
        // 벡터 차원 조정
        float[] adjustedVector = VectorUtils.adjustVectorDimension(queryVector, VECTOR_DIM);
        String vectorString = VectorUtils.vectorToString(adjustedVector);
        
        String sql = "SELECT id, title, content, embedding, created_at, updated_at, " +
                "1 - (embedding <=> ?::vector) as similarity " +
                "FROM documents " +
                "ORDER BY embedding <=> ?::vector " +
                "LIMIT ?";
        
        return jdbcTemplate.query(sql, documentRowMapper, vectorString, vectorString, limit);
    }
}
