package com.devahn.common;

import com.devahn.exception.DocumentException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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
