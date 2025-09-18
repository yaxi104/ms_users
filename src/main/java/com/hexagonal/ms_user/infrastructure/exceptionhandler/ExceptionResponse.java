package com.hexagonal.ms_user.infrastructure.exceptionhandler;

public enum ExceptionResponse {
    USER_ALREADY_EXISTS("User already exists"),
    BAD_REQUEST_MESSAGE("The request contains invalid data. Please check the submitted fields and try again"),
    INTERNAL_ERROR("An unexpected error occurred");
    private String message;

    ExceptionResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}