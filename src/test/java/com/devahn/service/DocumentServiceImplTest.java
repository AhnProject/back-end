package com.devahn.service;

import com.devahn.domain.document.Document;
import com.devahn.dto.request.CreateDocumentRequest;
import com.devahn.dto.request.SearchDocumentRequest;
import com.devahn.dto.request.UpdateDocumentRequest;
import com.devahn.dto.response.DocumentResponse;
import com.devahn.exception.DocumentException;
import com.devahn.repository.DocumentRepository;
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

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * DocumentService 단위 테스트
 */
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
                .title("테스트 문서")
                .content("테스트 내용입니다.")
                .embedding(testEmbedding)
                .build();

        updateRequest = UpdateDocumentRequest.builder()
                .title("수정된 문서")
                .content("수정된 내용입니다.")
                .embedding(testEmbedding)
                .build();

        document = Document.builder()
                .id(1L)
                .title("테스트 문서")
                .content("테스트 내용입니다.")
                .embedding(testEmbedding)
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("문서 생성 성공")
    void createDocument_Success() {
        // given
        when(documentRepository.save(any(Document.class))).thenReturn(1L);

        // when
        Long documentId = documentService.createDocument(createRequest);

        // then
        assertThat(documentId).isEqualTo(1L);
        verify(documentRepository, times(1)).save(any(Document.class));
    }

    @Test
    @DisplayName("문서 생성 실패 - 제목 없음")
    void createDocument_Fail_NoTitle() {
        // given
        CreateDocumentRequest invalidRequest = CreateDocumentRequest.builder()
                .title(null)
                .content("내용만 있음")
                .build();

        // when & then
        assertThatThrownBy(() -> documentService.createDocument(invalidRequest))
                .isInstanceOf(DocumentException.class)
                .hasMessageContaining("제목은 필수입니다");

        verify(documentRepository, never()).save(any(Document.class));
    }

    @Test
    @DisplayName("문서 생성 실패 - 내용 없음")
    void createDocument_Fail_NoContent() {
        // given
        CreateDocumentRequest invalidRequest = CreateDocumentRequest.builder()
                .title("제목만 있음")
                .content(null)
                .build();

        // when & then
        assertThatThrownBy(() -> documentService.createDocument(invalidRequest))
                .isInstanceOf(DocumentException.class)
                .hasMessageContaining("내용은 필수입니다");

        verify(documentRepository, never()).save(any(Document.class));
    }

    @Test
    @DisplayName("문서 조회 성공")
    void getDocument_Success() {
        // given
        when(documentRepository.findById(1L)).thenReturn(Optional.of(document));

        // when
        DocumentResponse response = documentService.getDocument(1L);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getTitle()).isEqualTo("테스트 문서");
        verify(documentRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("문서 조회 실패 - 존재하지 않는 ID")
    void getDocument_Fail_NotFound() {
        // given
        when(documentRepository.findById(999L)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> documentService.getDocument(999L))
                .isInstanceOf(DocumentException.class)
                .hasMessageContaining("문서를 찾을 수 없습니다");

        verify(documentRepository, times(1)).findById(999L);
    }

    @Test
    @DisplayName("전체 문서 조회 성공")
    void getAllDocuments_Success() {
        // given
        Document document2 = Document.builder()
                .id(2L)
                .title("두 번째 문서")
                .content("두 번째 내용")
                .embedding(testEmbedding)
                .createdAt(LocalDateTime.now())
                .build();

        when(documentRepository.findAll()).thenReturn(Arrays.asList(document, document2));

        // when
        List<DocumentResponse> responses = documentService.getAllDocuments();

        // then
        assertThat(responses).hasSize(2);
        assertThat(responses.get(0).getId()).isEqualTo(1L);
        assertThat(responses.get(1).getId()).isEqualTo(2L);
        verify(documentRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("문서 수정 성공")
    void updateDocument_Success() {
        // given
        when(documentRepository.findById(1L)).thenReturn(Optional.of(document));
        when(documentRepository.update(any(Document.class))).thenReturn(1);

        // when
        documentService.updateDocument(1L, updateRequest);

        // then
        verify(documentRepository, times(1)).findById(1L);
        verify(documentRepository, times(1)).update(any(Document.class));
    }

    @Test
    @DisplayName("문서 수정 실패 - 존재하지 않는 ID")
    void updateDocument_Fail_NotFound() {
        // given
        when(documentRepository.findById(999L)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> documentService.updateDocument(999L, updateRequest))
                .isInstanceOf(DocumentException.class)
                .hasMessageContaining("문서를 찾을 수 없습니다");

        verify(documentRepository, times(1)).findById(999L);
        verify(documentRepository, never()).update(any(Document.class));
    }

    @Test
    @DisplayName("문서 삭제 성공")
    void deleteDocument_Success() {
        // given
        when(documentRepository.findById(1L)).thenReturn(Optional.of(document));
        when(documentRepository.deleteById(1L)).thenReturn(1);

        // when
        documentService.deleteDocument(1L);

        // then
        verify(documentRepository, times(1)).findById(1L);
        verify(documentRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("문서 삭제 실패 - 존재하지 않는 ID")
    void deleteDocument_Fail_NotFound() {
        // given
        when(documentRepository.findById(999L)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> documentService.deleteDocument(999L))
                .isInstanceOf(DocumentException.class)
                .hasMessageContaining("문서를 찾을 수 없습니다");

        verify(documentRepository, times(1)).findById(999L);
        verify(documentRepository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("벡터 검색 성공")
    void searchDocuments_Success() {
        // given
        SearchDocumentRequest searchRequest = SearchDocumentRequest.builder()
                .embedding(testEmbedding)
                .limit(5)
                .threshold(0.7f)
                .build();

        when(documentRepository.searchByVector(any(float[].class), anyInt()))
                .thenReturn(Arrays.asList(document));

        // when
        List<DocumentResponse> responses = documentService.searchDocuments(searchRequest);

        // then
        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).getId()).isEqualTo(1L);
        verify(documentRepository, times(1)).searchByVector(any(float[].class), eq(5));
    }

    @Test
    @DisplayName("벡터 검색 실패 - 빈 벡터")
    void searchDocuments_Fail_EmptyVector() {
        // given
        SearchDocumentRequest searchRequest = SearchDocumentRequest.builder()
                .embedding(new float[0])
                .limit(5)
                .build();

        // when & then
        assertThatThrownBy(() -> documentService.searchDocuments(searchRequest))
                .isInstanceOf(DocumentException.class)
                .hasMessageContaining("검색 벡터가 비어있습니다");

        verify(documentRepository, never()).searchByVector(any(float[].class), anyInt());
    }
}
