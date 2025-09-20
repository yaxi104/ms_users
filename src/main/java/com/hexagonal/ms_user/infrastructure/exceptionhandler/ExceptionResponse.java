package com.hexagonal.ms_user.infrastructure.exceptionhandler;

public enum ExceptionResponse {
    USER_ALREADY_EXISTS("User already exists"),
    BAD_REQUEST_MESSAGE("The request contains invalid data. Please check the submitted fields and try again"),
    UNATHORIZED_MESSAGE("Authentication is required to access this resource"),
    USER_NOT_FOUND("User not found"),
    FORBIDDEN_MESSAGE("You do not have permission to access this resource");
    private String message;

    ExceptionResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}