package com.devahn.common;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 표준화된 API 성공 응답 래퍼
 */
@Getter
@NoArgsConstructor
public class ApiResponse<T> {
    private boolean success;
    private T data;
    private String message;
    private long timestamp;

    public ApiResponse(T data, String message) {
        this.success = true;
        this.data = data;
        this.message = message;
        this.timestamp = System.currentTimeMillis();
    }

    public ApiResponse(T data) {
        this(data, "성공");
    }

    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(data, "성공");
    }

    public static <T> ApiResponse<T> ok(T data, String message) {
        return new ApiResponse<>(data, message);
    }
}
