package com.devahn.app.api;

import com.devahn.support.exception.AuthException;
import com.devahn.support.exception.DocumentException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice(name = "apiGlobalExceptionHandler")
public class GlobalExceptionHandler {

    @ExceptionHandler(DocumentException.class)
    public ResponseEntity<ErrorResponse> handleDocumentException(DocumentException e) {
        log.error("Document exception: [{}] {}", e.getErrorCode(), e.getMessage(), e);
        return ResponseEntity.status(e.getHttpStatus())
                .body(ErrorResponse.of(e.getErrorCode(), e.getMessage(), e.getHttpStatus()));
    }

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<ErrorResponse> handleAuthException(AuthException e) {
        log.error("Auth exception: [{}] {}", e.getErrorCode(), e.getMessage());
        return ResponseEntity.status(e.getHttpStatus())
                .body(ErrorResponse.of(e.getErrorCode(), e.getMessage(), e.getHttpStatus()));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException e) {
        log.error("Authentication failed: {}", e.getMessage());
        return ResponseEntity.status(401)
                .body(ErrorResponse.of("AUTHENTICATION_FAILED", "Authentication failed", 401));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException e) {
        log.error("Access denied: {}", e.getMessage());
        return ResponseEntity.status(403)
                .body(ErrorResponse.of("ACCESS_DENIED", "Access denied", 403));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        String message = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        log.warn("Validation failed: {}", message);
        return ResponseEntity.badRequest()
                .body(ErrorResponse.of("INVALID_INPUT", "Validation failed: " + message, 400));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception e) {
        log.error("Unexpected server error", e);
        return ResponseEntity.status(500)
                .body(ErrorResponse.of("INTERNAL_SERVER_ERROR", "Internal server error", 500));
    }
}
