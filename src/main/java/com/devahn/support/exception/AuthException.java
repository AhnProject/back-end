package com.devahn.support.exception;

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
        return new AuthException("Username already exists: " + username, "USERNAME_ALREADY_EXISTS", 400);
    }

    public static AuthException emailAlreadyExists(String email) {
        return new AuthException("Email already exists: " + email, "EMAIL_ALREADY_EXISTS", 400);
    }

    public static AuthException invalidCredentials() {
        return new AuthException("Invalid username or password", "INVALID_CREDENTIALS", 401);
    }

    public static AuthException userNotFound(String username) {
        return new AuthException("User not found: " + username, "USER_NOT_FOUND", 404);
    }

    public static AuthException tokenExpired() {
        return new AuthException("Token expired", "TOKEN_EXPIRED", 401);
    }

    public static AuthException invalidToken() {
        return new AuthException("Invalid token", "INVALID_TOKEN", 401);
    }
}
