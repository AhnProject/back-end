package com.devahn.repository;

import com.devahn.domain.document.Document;
import java.util.List;
import java.util.Optional;

/**
 * Document Repository 인터페이스
 * 데이터 액세스 계층
 */
public interface DocumentRepository {
    
    /**
     * 문서 저장
     */
    Long save(Document document);
    
    /**
     * ID로 문서 조회
     */
    Optional<Document> findById(Long id);
    
    /**
     * 모든 문서 조회
     */
    List<Document> findAll();
    
    /**
     * 문서 수정
     */
    int update(Document document);
    
    /**
     * 문서 삭제
     */
    int deleteById(Long id);
    
    /**
     * 벡터 유사도 검색
     */
    List<Document> searchByVector(float[] queryVector, int limit);
}
