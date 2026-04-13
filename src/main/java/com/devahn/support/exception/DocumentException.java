package com.devahn.support.exception;

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

    public static DocumentException notFound(Long documentId) {
        return new DocumentException(
                "Document not found. ID: " + documentId,
                "DOCUMENT_NOT_FOUND",
                404
        );
    }

    public static DocumentException invalidVectorDimension(int expectedDim, int actualDim) {
        return new DocumentException(
                String.format("Invalid vector dimension. Expected: %d, actual: %d", expectedDim, actualDim),
                "INVALID_VECTOR_DIMENSION",
                400
        );
    }

    public static DocumentException databaseError(String message, Throwable cause) {
        return new DocumentException(
                "Database error: " + message,
                "DATABASE_ERROR",
                500,
                cause
        );
    }

    public static DocumentException invalidInput(String message) {
        return new DocumentException(
                "Invalid input: " + message,
                "INVALID_INPUT",
                400
        );
    }
}
