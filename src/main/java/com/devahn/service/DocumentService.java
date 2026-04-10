package com.devahn.service;

import com.devahn.dto.request.CreateDocumentRequest;
import com.devahn.dto.request.SearchDocumentRequest;
import com.devahn.dto.request.UpdateDocumentRequest;
import com.devahn.dto.response.DocumentResponse;

import java.util.List;

/**
 * Document Service 인터페이스
 * 비즈니스 로직 계층
 */
public interface DocumentService {
    
    /**
     * 문서 생성
     */
    Long createDocument(CreateDocumentRequest request);
    
    /**
     * 문서 조회 (ID)
     */
    DocumentResponse getDocument(Long id);
    
    /**
     * 모든 문서 조회
     */
    List<DocumentResponse> getAllDocuments();
    
    /**
     * 문서 수정
     */
    void updateDocument(Long id, UpdateDocumentRequest request);
    
    /**
     * 문서 삭제
     */
    void deleteDocument(Long id);
    
    /**
     * 벡터 유사도 검색
     */
    List<DocumentResponse> searchDocuments(SearchDocumentRequest request);
}
