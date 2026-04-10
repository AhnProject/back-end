package com.devahn.controller;

import com.devahn.common.ApiResponse;
import com.devahn.dto.request.CreateDocumentRequest;
import com.devahn.dto.request.SearchDocumentRequest;
import com.devahn.dto.request.UpdateDocumentRequest;
import com.devahn.dto.response.DocumentResponse;
import com.devahn.service.DocumentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Document Controller
 * REST API 엔드포인트 정의
 */
@Slf4j
@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
@Tag(name = "문서", description = "문서 관리 및 벡터 검색 API")
@SecurityRequirement(name = "Bearer Authentication")
public class DocumentController {

    private final DocumentService documentService;

    @Operation(
        summary = "문서 생성",
        description = "새로운 문서를 생성합니다. 벡터 임베딩은 선택사항이며, 미제공 시 자동으로 0으로 채워집니다."
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "201",
            description = "문서 생성 성공",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(value = """
                    {
                      "success": true,
                      "data": 1,
                      "message": "문서가 성공적으로 생성되었습니다",
                      "errorCode": null,
                      "timestamp": 1234567890
                    }
                    """)
            )
        )
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<Long> createDocument(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "문서 생성 정보",
                required = true,
                content = @Content(
                    schema = @Schema(implementation = CreateDocumentRequest.class),
                    examples = @ExampleObject(value = """
                        {
                          "title": "샘플 문서",
                          "content": "이것은 샘플 문서 내용입니다.",
                          "embedding": [0.1, 0.2, 0.3, 0.4, 0.5]
                        }
                        """)
                )
            )
            @Valid @RequestBody CreateDocumentRequest request) {
        log.info("문서 생성 요청 - 제목: {}", request.getTitle());
        Long documentId = documentService.createDocument(request);
        return ApiResponse.ok(documentId, "문서가 성공적으로 생성되었습니다");
    }

    @Operation(
        summary = "문서 조회",
        description = "ID로 특정 문서를 조회합니다."
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "문서 조회 성공"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "문서를 찾을 수 없음"
        )
    })
    @GetMapping("/{id}")
    public ApiResponse<DocumentResponse> getDocument(@PathVariable Long id) {
        log.info("문서 조회 요청 - ID: {}", id);
        DocumentResponse response = documentService.getDocument(id);
        return ApiResponse.ok(response, "문서 조회 성공");
    }

    @Operation(
        summary = "전체 문서 조회",
        description = "모든 문서를 조회합니다. 생성일시 기준 내림차순으로 정렬됩니다."
    )
    @GetMapping
    public ApiResponse<List<DocumentResponse>> getAllDocuments() {
        log.info("전체 문서 조회 요청");
        List<DocumentResponse> documents = documentService.getAllDocuments();
        return ApiResponse.ok(documents, String.format("총 %d건의 문서를 조회했습니다", documents.size()));
    }

    @Operation(
        summary = "문서 수정",
        description = "기존 문서의 내용을 수정합니다."
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "문서 수정 성공"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "문서를 찾을 수 없음"
        )
    })
    @PutMapping("/{id}")
    public ApiResponse<Void> updateDocument(
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "수정할 문서 정보",
                required = true,
                content = @Content(
                    schema = @Schema(implementation = UpdateDocumentRequest.class),
                    examples = @ExampleObject(value = """
                        {
                          "title": "수정된 문서 제목",
                          "content": "수정된 문서 내용입니다.",
                          "embedding": [0.15, 0.25, 0.35, 0.45, 0.55]
                        }
                        """)
                )
            )
            @Valid @RequestBody UpdateDocumentRequest request) {
        log.info("문서 수정 요청 - ID: {}, 제목: {}", id, request.getTitle());
        documentService.updateDocument(id, request);
        return ApiResponse.ok(null, "문서가 성공적으로 수정되었습니다");
    }

    @Operation(
        summary = "문서 삭제",
        description = "ID로 특정 문서를 삭제합니다."
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "문서 삭제 성공"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "문서를 찾을 수 없음"
        )
    })
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteDocument(@PathVariable Long id) {
        log.info("문서 삭제 요청 - ID: {}", id);
        documentService.deleteDocument(id);
        return ApiResponse.ok(null, "문서가 성공적으로 삭제되었습니다");
    }

    @Operation(
        summary = "벡터 유사도 검색",
        description = "제공된 벡터와 유사한 문서를 검색합니다. 코사인 유사도 기반으로 정렬됩니다."
    )
    @PostMapping("/search")
    public ApiResponse<List<DocumentResponse>> searchDocuments(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "벡터 검색 조건",
                required = true,
                content = @Content(
                    schema = @Schema(implementation = SearchDocumentRequest.class),
                    examples = @ExampleObject(value = """
                        {
                          "embedding": [0.2, 0.3, 0.4, 0.5, 0.6],
                          "limit": 10,
                          "threshold": 0.7
                        }
                        """)
                )
            )
            @Valid @RequestBody SearchDocumentRequest request) {
        log.info("벡터 검색 요청 - 리미트: {}", request.getLimit());
        List<DocumentResponse> documents = documentService.searchDocuments(request);
        return ApiResponse.ok(documents, String.format("총 %d건의 유사 문서를 찾았습니다", documents.size()));
    }

    @Operation(
        summary = "헬스 체크",
        description = "API 서버 상태를 확인합니다."
    )
    @GetMapping("/health")
    public ApiResponse<String> healthCheck() {
        return ApiResponse.ok("OK", "Document API is running");
    }
}
