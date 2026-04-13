package com.devahn.document.service;

import com.devahn.document.domain.Document;
import com.devahn.document.dto.CreateDocumentRequest;
import com.devahn.document.dto.DocumentResponse;
import com.devahn.document.dto.SearchDocumentRequest;
import com.devahn.document.dto.UpdateDocumentRequest;
import com.devahn.document.repository.DocumentRepository;
import com.devahn.support.exception.DocumentException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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
            validateDocumentRequest(request.getTitle(), request.getContent());

            Document document = Document.builder()
                    .title(request.getTitle())
                    .content(request.getContent())
                    .embedding(request.getEmbedding())
                    .build();

            Long documentId = documentRepository.save(document);
            log.info("Document created: id={}, title={}", documentId, request.getTitle());
            return documentId;
        } catch (DataAccessException e) {
            log.error("Document creation failed: title={}", request.getTitle(), e);
            throw DocumentException.databaseError("Failed to save document", e);
        }
    }

    @Override
    public DocumentResponse getDocument(Long id) {
        Document document = documentRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Document not found: id={}", id);
                    return DocumentException.notFound(id);
                });

        log.info("Document fetched: id={}, title={}", id, document.getTitle());
        return convertToResponse(document);
    }

    @Override
    public List<DocumentResponse> getAllDocuments() {
        try {
            List<Document> documents = documentRepository.findAll();
            log.info("Documents listed: count={}", documents.size());

            return documents.stream()
                    .map(this::convertToResponse)
                    .collect(Collectors.toList());
        } catch (DataAccessException e) {
            log.error("Document listing failed", e);
            throw DocumentException.databaseError("Failed to load documents", e);
        }
    }

    @Override
    @Transactional
    public void updateDocument(Long id, UpdateDocumentRequest request) {
        try {
            documentRepository.findById(id)
                    .orElseThrow(() -> DocumentException.notFound(id));

            validateDocumentRequest(request.getTitle(), request.getContent());

            Document updatedDocument = Document.builder()
                    .id(id)
                    .title(request.getTitle())
                    .content(request.getContent())
                    .embedding(request.getEmbedding())
                    .build();

            int affectedRows = documentRepository.update(updatedDocument);
            if (affectedRows == 0) {
                throw DocumentException.databaseError("Update did not modify any rows", null);
            }

            log.info("Document updated: id={}, title={}", id, request.getTitle());
        } catch (DataAccessException e) {
            log.error("Document update failed: id={}", id, e);
            throw DocumentException.databaseError("Failed to update document", e);
        }
    }

    @Override
    @Transactional
    public void deleteDocument(Long id) {
        try {
            documentRepository.findById(id)
                    .orElseThrow(() -> DocumentException.notFound(id));

            int affectedRows = documentRepository.deleteById(id);
            if (affectedRows == 0) {
                throw DocumentException.databaseError("Delete did not modify any rows", null);
            }

            log.info("Document deleted: id={}", id);
        } catch (DataAccessException e) {
            log.error("Document delete failed: id={}", id, e);
            throw DocumentException.databaseError("Failed to delete document", e);
        }
    }

    @Override
    public List<DocumentResponse> searchDocuments(SearchDocumentRequest request) {
        try {
            if (request.getEmbedding() == null || request.getEmbedding().length == 0) {
                throw DocumentException.invalidInput("Search embedding must not be empty");
            }

            List<Document> documents = documentRepository.searchByVector(
                    request.getEmbedding(),
                    request.getLimit()
            );

            log.info("Document search completed: count={}", documents.size());
            return documents.stream()
                    .map(this::convertToResponse)
                    .collect(Collectors.toList());
        } catch (DataAccessException e) {
            log.error("Document search failed", e);
            throw DocumentException.databaseError("Failed to search documents", e);
        }
    }

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

    private void validateDocumentRequest(String title, String content) {
        if (title == null || title.trim().isEmpty()) {
            throw DocumentException.invalidInput("Title is required");
        }
        if (content == null || content.trim().isEmpty()) {
            throw DocumentException.invalidInput("Content is required");
        }
        if (title.length() > 255) {
            throw DocumentException.invalidInput("Title must not exceed 255 characters");
        }
        if (content.length() > 10000) {
            throw DocumentException.invalidInput("Content must not exceed 10000 characters");
        }
    }
}
