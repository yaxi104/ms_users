package com.hexagonal.ms_user.infrastructure.exceptionhandler;

import com.hexagonal.ms_user.domain.exception.BadRequestException;
import com.hexagonal.ms_user.domain.exception.UserAlreadyExistsException;
import com.hexagonal.ms_user.domain.exception.UserNotFoundException;
import com.hexagonal.ms_user.domain.exception.UserNotOlderAgeException;
import com.hexagonal.ms_user.infrastructure.exception.UserForbiddenException;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.naming.AuthenticationException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

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
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);

        ResponseEntity<Map<String, String>> response = controllerAdvisor.handleValidationErrors(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(ExceptionResponse.BAD_REQUEST_MESSAGE.getMessage(), response.getBody().get(MESSAGE));
    }

    @Test
    void handleBadRequestExceptionReturnsBadRequest() {
        BadRequestException ex = mock(BadRequestException.class);

        ResponseEntity<Map<String, String>> response = controllerAdvisor.handleBadRequestException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(ExceptionResponse.BAD_REQUEST_MESSAGE.getMessage(), response.getBody().get(MESSAGE));
    }

    @Test
    void handleUserNonFoundExceptionReturnsForbidden() {
        UserForbiddenException ex = mock(UserForbiddenException.class);

        ResponseEntity<Map<String, String>> response = controllerAdvisor.handleUserNonFoundException(ex);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(ExceptionResponse.FORBIDDEN_MESSAGE.getMessage(), response.getBody().get(MESSAGE));
    }

    @Test
    void handleAuthenticationExceptionReturnsUnathorization() {
        AuthenticationException ex = mock(AuthenticationException.class);

        ResponseEntity<Map<String, String>> response = controllerAdvisor.handleAuthenticationException(ex);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(ExceptionResponse.UNATHORIZED_MESSAGE.getMessage(), response.getBody().get(MESSAGE));
    }

    @Test
    void handleUserNotFoundExceptionReturnsNotFound() {
        UserNotFoundException ex = mock(UserNotFoundException.class);

        ResponseEntity<Map<String, String>> response = controllerAdvisor.handleUserNotFoundException(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(ExceptionResponse.USER_NOT_FOUND.getMessage(), response.getBody().get(MESSAGE));
    }

    @Test
    void handleConstraintViolationExceptionReturnsBadRequest() {
        ConstraintViolationException ex = mock(ConstraintViolationException.class);

        ResponseEntity<Map<String, String>> response = controllerAdvisor.handleConstraintViolationException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(ExceptionResponse.BAD_REQUEST_MESSAGE.getMessage(), response.getBody().get(MESSAGE));
    }

    @Test
    void handleTypeMismatchExceptionReturnsBadRequest() {
        MethodArgumentTypeMismatchException ex = mock(MethodArgumentTypeMismatchException.class);

        ResponseEntity<Map<String, String>> response = controllerAdvisor.handleTypeMismatch(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(ExceptionResponse.BAD_REQUEST_MESSAGE.getMessage(), response.getBody().get(MESSAGE));
    }
}