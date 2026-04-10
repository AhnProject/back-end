package com.devahn.exception;

public class AuthException extends RuntimeException {
    
    private final String errorCode;
    private final int httpStatus;

    public AuthException(String message, String errorCode, int httpStatus) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public static AuthException usernameAlreadyExists(String username) {
        return new AuthException(
            "이미 사용 중인 사용자명입니다: " + username,
            "USERNAME_ALREADY_EXISTS",
            400
        );
    }

    public static AuthException emailAlreadyExists(String email) {
        return new AuthException(
            "이미 사용 중인 이메일입니다: " + email,
            "EMAIL_ALREADY_EXISTS",
            400
        );
    }

    public static AuthException invalidCredentials() {
        return new AuthException(
            "사용자명 또는 비밀번호가 올바르지 않습니다",
            "INVALID_CREDENTIALS",
            401
        );
    }

    public static AuthException userNotFound(String username) {
        return new AuthException(
            "사용자를 찾을 수 없습니다: " + username,
            "USER_NOT_FOUND",
            404
        );
    }

    public static AuthException tokenExpired() {
        return new AuthException(
            "토큰이 만료되었습니다",
            "TOKEN_EXPIRED",
            401
        );
    }

    public static AuthException invalidToken() {
        return new AuthException(
            "유효하지 않은 토큰입니다",
            "INVALID_TOKEN",
            401
        );
    }
}
