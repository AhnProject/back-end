package com.devahn.exception;

/**
 * 문서 관련 비즈니스 로직 예외
 */
public class DocumentException extends RuntimeException {
    
    private final String errorCode;
    private final int httpStatus;

    public DocumentException(String message, String errorCode, int httpStatus) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }

    public DocumentException(String message, String errorCode, int httpStatus, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    /**
     * 문서 미존재 예외
     */
    public static DocumentException notFound(Long documentId) {
        return new DocumentException(
            "문서를 찾을 수 없습니다. ID: " + documentId,
            "DOCUMENT_NOT_FOUND",
            404
        );
    }

    /**
     * 유효하지 않은 벡터 차원 예외
     */
    public static DocumentException invalidVectorDimension(int expectedDim, int actualDim) {
        return new DocumentException(
            String.format("벡터 차원이 올바르지 않습니다. 예상: %d, 실제: %d", expectedDim, actualDim),
            "INVALID_VECTOR_DIMENSION",
            400
        );
    }

    /**
     * 데이터베이스 작업 실패
     */
    public static DocumentException databaseError(String message, Throwable cause) {
        return new DocumentException(
            "데이터베이스 작업 실패: " + message,
            "DATABASE_ERROR",
            500,
            cause
        );
    }

    /**
     * 유효하지 않은 입력
     */
    public static DocumentException invalidInput(String message) {
        return new DocumentException(
            "유효하지 않은 입력: " + message,
            "INVALID_INPUT",
            400
        );
    }
}
