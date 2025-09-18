package com.hexagonal.ms_user.infrastructure.exceptionhandler;

import com.hexagonal.ms_user.infrastructure.exception.UserAlreadyExistsException;
import com.hexagonal.ms_user.infrastructure.exception.UserNotOlderAgeException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ControllerAdvisorTest {

    private final ControllerAdvisor controllerAdvisor = new ControllerAdvisor();

    private static final String MESSAGE = "Message";

    @Test
    void handleUserAlreadyExistsExceptionReturnsConflict() {
        UserAlreadyExistsException ex = new UserAlreadyExistsException();

        ResponseEntity<Map<String, String>> response = controllerAdvisor.handleUserAlreadyExistsException(ex);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(ExceptionResponse.USER_ALREADY_EXISTS.getMessage(), response.getBody().get(MESSAGE));
    }

    @Test
    void handleUserNotOlderAgeExceptionReturnsBadRequest() {
        UserNotOlderAgeException ex = new UserNotOlderAgeException();

        ResponseEntity<Map<String, String>> response = controllerAdvisor.handleUserNotOlderAgeException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(ExceptionResponse.BAD_REQUEST_MESSAGE.getMessage(), response.getBody().get(MESSAGE));
    }

    @Test
    void handleValidationErrorsReturnsBadRequest() {
        MethodArgumentNotValidException ex = org.mockito.Mockito.mock(MethodArgumentNotValidException.class);

        ResponseEntity<Map<String, String>> response = controllerAdvisor.handleValidationErrors(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(ExceptionResponse.BAD_REQUEST_MESSAGE.getMessage(), response.getBody().get(MESSAGE));
    }
}