package com.devahn.document.controller;

import com.devahn.app.api.ApiResponse;
import com.devahn.document.dto.CreateDocumentRequest;
import com.devahn.document.dto.DocumentResponse;
import com.devahn.document.dto.SearchDocumentRequest;
import com.devahn.document.dto.UpdateDocumentRequest;
import com.devahn.document.service.DocumentService;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
@Tag(name = "Document", description = "Document management and vector search API")
@SecurityRequirement(name = "bearerAuth")
public class DocumentController {

    private final DocumentService documentService;

    @Operation(summary = "Create document", description = "Create a document. Missing embeddings are stored as zero vectors.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Document created")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<Long> createDocument(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Create document request body",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = CreateDocumentRequest.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "title": "Sample document",
                                      "content": "This is a sample document.",
                                      "embedding": [0.1, 0.2, 0.3, 0.4, 0.5]
                                    }
                                    """)
                    )
            )
            @Valid @RequestBody CreateDocumentRequest request) {
        log.info("Create document request: title={}", request.getTitle());
        Long documentId = documentService.createDocument(request);
        return ApiResponse.ok(documentId, "Document created");
    }

    @Operation(summary = "Get document", description = "Get a document by id.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Document found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Document not found")
    })
    @GetMapping("/{id}")
    public ApiResponse<DocumentResponse> getDocument(@PathVariable Long id) {
        log.info("Get document request: id={}", id);
        DocumentResponse response = documentService.getDocument(id);
        return ApiResponse.ok(response, "Document found");
    }

    @Operation(summary = "List documents", description = "List all documents ordered by newest first.")
    @GetMapping
    public ApiResponse<List<DocumentResponse>> getAllDocuments() {
        log.info("List documents request");
        List<DocumentResponse> documents = documentService.getAllDocuments();
        return ApiResponse.ok(documents, String.format("Returned %d document(s)", documents.size()));
    }

    @Operation(summary = "Update document", description = "Update title, content, and embedding for a document.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Document updated"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Document not found")
    })
    @PutMapping("/{id}")
    public ApiResponse<Void> updateDocument(
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Update document request body",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = UpdateDocumentRequest.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "title": "Updated document title",
                                      "content": "Updated document content.",
                                      "embedding": [0.15, 0.25, 0.35, 0.45, 0.55]
                                    }
                                    """)
                    )
            )
            @Valid @RequestBody UpdateDocumentRequest request) {
        log.info("Update document request: id={}, title={}", id, request.getTitle());
        documentService.updateDocument(id, request);
        return ApiResponse.ok(null, "Document updated");
    }

    @Operation(summary = "Delete document", description = "Delete a document by id.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Document deleted"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Document not found")
    })
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteDocument(@PathVariable Long id) {
        log.info("Delete document request: id={}", id);
        documentService.deleteDocument(id);
        return ApiResponse.ok(null, "Document deleted");
    }

    @Operation(summary = "Search by vector", description = "Search similar documents using an embedding and limit.")
    @PostMapping("/search")
    public ApiResponse<List<DocumentResponse>> searchDocuments(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Vector search request body",
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
        log.info("Search documents request: limit={}, threshold={}", request.getLimit(), request.getThreshold());
        List<DocumentResponse> documents = documentService.searchDocuments(request);
        return ApiResponse.ok(documents, String.format("Found %d similar document(s)", documents.size()));
    }

    @Operation(summary = "Health check", description = "Check whether the document API is running.")
    @GetMapping("/health")
    public ApiResponse<String> healthCheck() {
        return ApiResponse.ok("OK", "Document API is running");
    }
}
