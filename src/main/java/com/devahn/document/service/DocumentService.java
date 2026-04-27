package com.devahn.document.service;

import com.devahn.document.dto.CreateDocumentRequest;
import com.devahn.document.dto.DocumentResponse;
import com.devahn.document.dto.SearchDocumentRequest;
import com.devahn.document.dto.UpdateDocumentRequest;

import java.util.List;

public interface DocumentService {

    Long createDocument(CreateDocumentRequest request);

    DocumentResponse getDocument(Long id);

    List<DocumentResponse> getAllDocuments();

    void updateDocument(Long id, UpdateDocumentRequest request);

    void deleteDocument(Long id);

    List<DocumentResponse> searchDocuments(SearchDocumentRequest request);
}
