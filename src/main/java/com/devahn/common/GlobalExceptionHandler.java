package com.devahn.common;

import com.devahn.exception.AuthException;
import com.devahn.exception.DocumentException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

/**
 * 전역 예외 처리 핸들러
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * DocumentException 처리
     */
    @ExceptionHandler(DocumentException.class)
    public ResponseEntity<ErrorResponse> handleDocumentException(DocumentException e) {
        log.error("DocumentException 발생: [{}] {}", e.getErrorCode(), e.getMessage(), e);
        ErrorResponse errorResponse = ErrorResponse.of(
            e.getErrorCode(),
            e.getMessage(),
            e.getHttpStatus()
        );
        return ResponseEntity.status(e.getHttpStatus()).body(errorResponse);
    }

    /**
     * AuthException 처리
     */
    @ExceptionHandler(AuthException.class)
    public ResponseEntity<ErrorResponse> handleAuthException(AuthException e) {
        log.error("AuthException 발생: [{}] {}", e.getErrorCode(), e.getMessage());
        ErrorResponse errorResponse = ErrorResponse.of(
            e.getErrorCode(),
            e.getMessage(),
            e.getHttpStatus()
        );
        return ResponseEntity.status(e.getHttpStatus()).body(errorResponse);
    }

    /**
     * Spring Security AuthenticationException 처리
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException e) {
        log.error("인증 실패: {}", e.getMessage());
        ErrorResponse errorResponse = ErrorResponse.of(
            "AUTHENTICATION_FAILED",
            "인증에 실패했습니다",
            401
        );
        return ResponseEntity.status(401).body(errorResponse);
    }

    /**
     * Spring Security AccessDeniedException 처리
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException e) {
        log.error("접근 거부: {}", e.getMessage());
        ErrorResponse errorResponse = ErrorResponse.of(
            "ACCESS_DENIED",
            "접근 권한이 없습니다",
            403
        );
        return ResponseEntity.status(403).body(errorResponse);
    }

    /**
     * 입력 검증 실패 처리 (@Valid)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        String message = e.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(error -> error.getField() + ": " + error.getDefaultMessage())
            .collect(Collectors.joining(", "));
        
        log.warn("입력 검증 실패: {}", message);
        ErrorResponse errorResponse = ErrorResponse.of(
            "INVALID_INPUT",
            "입력 검증 실패: " + message,
            400
        );
        return ResponseEntity.badRequest().body(errorResponse);
    }

    /**
     * 기타 예외 처리
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception e) {
        log.error("예상치 못한 에러 발생", e);
        ErrorResponse errorResponse = ErrorResponse.of(
            "INTERNAL_SERVER_ERROR",
            "서버 오류가 발생했습니다. 관리자에게 문의하세요.",
            500
        );
        return ResponseEntity.status(500).body(errorResponse);
    }
}
