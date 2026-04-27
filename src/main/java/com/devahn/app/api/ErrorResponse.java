package com.devahn.app.api;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ErrorResponse {
    private String errorCode;
    private String message;
    private int status;
    private long timestamp;

    public ErrorResponse(String errorCode, String message, int status) {
        this.errorCode = errorCode;
        this.message = message;
        this.status = status;
        this.timestamp = System.currentTimeMillis();
    }

    public static ErrorResponse of(String errorCode, String message, int status) {
        return new ErrorResponse(errorCode, message, status);
    }
}
