package com.devahn.service;

import com.devahn.domain.document.Document;
import com.devahn.dto.request.CreateDocumentRequest;
import com.devahn.dto.request.SearchDocumentRequest;
import com.devahn.dto.request.UpdateDocumentRequest;
import com.devahn.dto.response.DocumentResponse;
import com.devahn.exception.DocumentException;
import com.devahn.repository.DocumentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Document Service 구현체
 * 비즈니스 로직 처리
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;

    @Override
    @Transactional
    public Long createDocument(CreateDocumentRequest request) {
        try {
            // 입력 검증
            validateDocumentRequest(request.getTitle(), request.getContent());
            
            // DTO -> Entity 변환
            Document document = Document.builder()
                    .title(request.getTitle())
                    .content(request.getContent())
                    .embedding(request.getEmbedding())
                    .build();
            
            // 저장
            Long documentId = documentRepository.save(document);
            log.info("문서 생성 완료 - ID: {}, 제목: {}", documentId, request.getTitle());
            
            return documentId;
        } catch (DataAccessException e) {
            log.error("문서 생성 실패 - 제목: {}", request.getTitle(), e);
            throw DocumentException.databaseError("문서 저장 중 오류가 발생했습니다", e);
        }
    }

    @Override
    public DocumentResponse getDocument(Long id) {
        Document document = documentRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("문서 조회 실패 - ID: {}", id);
                    return DocumentException.notFound(id);
                });
        
        log.info("문서 조회 완료 - ID: {}, 제목: {}", id, document.getTitle());
        return convertToResponse(document);
    }

    @Override
    public List<DocumentResponse> getAllDocuments() {
        try {
            List<Document> documents = documentRepository.findAll();
            log.info("전체 문서 조회 완료 - 개수: {}", documents.size());
            
            return documents.stream()
                    .map(this::convertToResponse)
                    .collect(Collectors.toList());
        } catch (DataAccessException e) {
            log.error("전체 문서 조회 실패", e);
            throw DocumentException.databaseError("문서 목록 조회 중 오류가 발생했습니다", e);
        }
    }

    @Override
    @Transactional
    public void updateDocument(Long id, UpdateDocumentRequest request) {
        try {
            // 문서 존재 확인
            Document existingDocument = documentRepository.findById(id)
                    .orElseThrow(() -> DocumentException.notFound(id));
            
            // 입력 검증
            validateDocumentRequest(request.getTitle(), request.getContent());
            
            // 업데이트할 Document 생성
            Document updatedDocument = Document.builder()
                    .id(id)
                    .title(request.getTitle())
                    .content(request.getContent())
                    .embedding(request.getEmbedding())
                    .build();
            
            // 업데이트 실행
            int affectedRows = documentRepository.update(updatedDocument);
            
            if (affectedRows == 0) {
                throw DocumentException.databaseError("문서 수정에 실패했습니다", null);
            }
            
            log.info("문서 수정 완료 - ID: {}, 제목: {}", id, request.getTitle());
        } catch (DataAccessException e) {
            log.error("문서 수정 실패 - ID: {}", id, e);
            throw DocumentException.databaseError("문서 수정 중 오류가 발생했습니다", e);
        }
    }

    @Override
    @Transactional
    public void deleteDocument(Long id) {
        try {
            // 문서 존재 확인
            documentRepository.findById(id)
                    .orElseThrow(() -> DocumentException.notFound(id));
            
            // 삭제 실행
            int affectedRows = documentRepository.deleteById(id);
            
            if (affectedRows == 0) {
                throw DocumentException.databaseError("문서 삭제에 실패했습니다", null);
            }
            
            log.info("문서 삭제 완료 - ID: {}", id);
        } catch (DataAccessException e) {
            log.error("문서 삭제 실패 - ID: {}", id, e);
            throw DocumentException.databaseError("문서 삭제 중 오류가 발생했습니다", e);
        }
    }

    @Override
    public List<DocumentResponse> searchDocuments(SearchDocumentRequest request) {
        try {
            // 벡터 검증
            if (request.getEmbedding() == null || request.getEmbedding().length == 0) {
                throw DocumentException.invalidInput("검색 벡터가 비어있습니다");
            }
            
            // 벡터 유사도 검색
            List<Document> documents = documentRepository.searchByVector(
                    request.getEmbedding(), 
                    request.getLimit()
            );
            
            log.info("벡터 검색 완료 - 결과 개수: {}", documents.size());
            
            return documents.stream()
                    .map(this::convertToResponse)
                    .collect(Collectors.toList());
        } catch (DataAccessException e) {
            log.error("벡터 검색 실패", e);
            throw DocumentException.databaseError("벡터 검색 중 오류가 발생했습니다", e);
        }
    }

    /**
     * Document Entity -> DocumentResponse DTO 변환
     */
    private DocumentResponse convertToResponse(Document document) {
        return DocumentResponse.builder()
                .id(document.getId())
                .title(document.getTitle())
                .content(document.getContent())
                .embedding(document.getEmbedding())
                .createdAt(document.getCreatedAt())
                .updatedAt(document.getUpdatedAt())
                .build();
    }

    /**
     * 문서 입력 검증
     */
    private void validateDocumentRequest(String title, String content) {
        if (title == null || title.trim().isEmpty()) {
            throw DocumentException.invalidInput("제목은 필수입니다");
        }
        if (content == null || content.trim().isEmpty()) {
            throw DocumentException.invalidInput("내용은 필수입니다");
        }
        if (title.length() > 255) {
            throw DocumentException.invalidInput("제목은 255자를 초과할 수 없습니다");
        }
        if (content.length() > 10000) {
            throw DocumentException.invalidInput("내용은 10000자를 초과할 수 없습니다");
        }
    }
}
