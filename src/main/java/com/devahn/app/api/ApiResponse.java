package com.devahn.app.api;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ApiResponse<T> {
    private boolean success;
    private T data;
    private String message;
    private String errorCode;
    private long timestamp;

    public ApiResponse(boolean success, T data, String message, String errorCode) {
        this.success = success;
        this.data = data;
        this.message = message;
        this.errorCode = errorCode;
        this.timestamp = System.currentTimeMillis();
    }

    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(true, data, "Success", null);
    }

    public static <T> ApiResponse<T> ok(T data, String message) {
        return new ApiResponse<>(true, data, message, null);
    }

    public static <T> ApiResponse<T> error(String errorCode, String message) {
        return new ApiResponse<>(false, null, message, errorCode);
    }
}
