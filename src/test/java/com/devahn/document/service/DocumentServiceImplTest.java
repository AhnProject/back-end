package com.devahn.document.service;

import com.devahn.document.domain.Document;
import com.devahn.document.dto.CreateDocumentRequest;
import com.devahn.document.dto.DocumentResponse;
import com.devahn.document.dto.SearchDocumentRequest;
import com.devahn.document.dto.UpdateDocumentRequest;
import com.devahn.document.repository.DocumentRepository;
import com.devahn.support.exception.DocumentException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DocumentServiceImplTest {

    @Mock
    private DocumentRepository documentRepository;

    @InjectMocks
    private DocumentServiceImpl documentService;

    private CreateDocumentRequest createRequest;
    private UpdateDocumentRequest updateRequest;
    private Document document;
    private float[] testEmbedding;

    @BeforeEach
    void setUp() {
        testEmbedding = new float[1536];
        Arrays.fill(testEmbedding, 0.1f);

        createRequest = CreateDocumentRequest.builder()
                .title("Test document")
                .content("Test content")
                .embedding(testEmbedding)
                .build();

        updateRequest = UpdateDocumentRequest.builder()
                .title("Updated document")
                .content("Updated content")
                .embedding(testEmbedding)
                .build();

        document = Document.builder()
                .id(1L)
                .title("Test document")
                .content("Test content")
                .embedding(testEmbedding)
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("create document succeeds")
    void createDocumentSuccess() {
        when(documentRepository.save(any(Document.class))).thenReturn(1L);

        Long documentId = documentService.createDocument(createRequest);

        assertThat(documentId).isEqualTo(1L);
        verify(documentRepository, times(1)).save(any(Document.class));
    }

    @Test
    @DisplayName("create document fails without title")
    void createDocumentFailNoTitle() {
        CreateDocumentRequest invalidRequest = CreateDocumentRequest.builder()
                .title(null)
                .content("content only")
                .build();

        assertThatThrownBy(() -> documentService.createDocument(invalidRequest))
                .isInstanceOf(DocumentException.class)
                .hasMessageContaining("Title is required");

        verify(documentRepository, never()).save(any(Document.class));
    }

    @Test
    @DisplayName("create document fails without content")
    void createDocumentFailNoContent() {
        CreateDocumentRequest invalidRequest = CreateDocumentRequest.builder()
                .title("title only")
                .content(null)
                .build();

        assertThatThrownBy(() -> documentService.createDocument(invalidRequest))
                .isInstanceOf(DocumentException.class)
                .hasMessageContaining("Content is required");

        verify(documentRepository, never()).save(any(Document.class));
    }

    @Test
    @DisplayName("get document succeeds")
    void getDocumentSuccess() {
        when(documentRepository.findById(1L)).thenReturn(Optional.of(document));

        DocumentResponse response = documentService.getDocument(1L);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getTitle()).isEqualTo("Test document");
        verify(documentRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("get document fails when missing")
    void getDocumentFailNotFound() {
        when(documentRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> documentService.getDocument(999L))
                .isInstanceOf(DocumentException.class)
                .hasMessageContaining("Document not found");

        verify(documentRepository, times(1)).findById(999L);
    }

    @Test
    @DisplayName("list documents succeeds")
    void getAllDocumentsSuccess() {
        Document document2 = Document.builder()
                .id(2L)
                .title("Second document")
                .content("Second content")
                .embedding(testEmbedding)
                .createdAt(LocalDateTime.now())
                .build();

        when(documentRepository.findAll()).thenReturn(Arrays.asList(document, document2));

        List<DocumentResponse> responses = documentService.getAllDocuments();

        assertThat(responses).hasSize(2);
        assertThat(responses.get(0).getId()).isEqualTo(1L);
        assertThat(responses.get(1).getId()).isEqualTo(2L);
        verify(documentRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("update document succeeds")
    void updateDocumentSuccess() {
        when(documentRepository.findById(1L)).thenReturn(Optional.of(document));
        when(documentRepository.update(any(Document.class))).thenReturn(1);

        documentService.updateDocument(1L, updateRequest);

        verify(documentRepository, times(1)).findById(1L);
        verify(documentRepository, times(1)).update(any(Document.class));
    }

    @Test
    @DisplayName("update document fails when missing")
    void updateDocumentFailNotFound() {
        when(documentRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> documentService.updateDocument(999L, updateRequest))
                .isInstanceOf(DocumentException.class)
                .hasMessageContaining("Document not found");

        verify(documentRepository, times(1)).findById(999L);
        verify(documentRepository, never()).update(any(Document.class));
    }

    @Test
    @DisplayName("delete document succeeds")
    void deleteDocumentSuccess() {
        when(documentRepository.findById(1L)).thenReturn(Optional.of(document));
        when(documentRepository.deleteById(1L)).thenReturn(1);

        documentService.deleteDocument(1L);

        verify(documentRepository, times(1)).findById(1L);
        verify(documentRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("delete document fails when missing")
    void deleteDocumentFailNotFound() {
        when(documentRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> documentService.deleteDocument(999L))
                .isInstanceOf(DocumentException.class)
                .hasMessageContaining("Document not found");

        verify(documentRepository, times(1)).findById(999L);
        verify(documentRepository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("search documents succeeds")
    void searchDocumentsSuccess() {
        SearchDocumentRequest searchRequest = SearchDocumentRequest.builder()
                .embedding(testEmbedding)
                .limit(5)
                .threshold(0.7f)
                .build();

        when(documentRepository.searchByVector(any(float[].class), anyInt()))
                .thenReturn(Arrays.asList(document));

        List<DocumentResponse> responses = documentService.searchDocuments(searchRequest);

        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).getId()).isEqualTo(1L);
        verify(documentRepository, times(1)).searchByVector(any(float[].class), eq(5));
    }

    @Test
    @DisplayName("search documents fails with empty vector")
    void searchDocumentsFailEmptyVector() {
        SearchDocumentRequest searchRequest = SearchDocumentRequest.builder()
                .embedding(new float[0])
                .limit(5)
                .build();

        assertThatThrownBy(() -> documentService.searchDocuments(searchRequest))
                .isInstanceOf(DocumentException.class)
                .hasMessageContaining("Search embedding must not be empty");

        verify(documentRepository, never()).searchByVector(any(float[].class), anyInt());
    }
}
