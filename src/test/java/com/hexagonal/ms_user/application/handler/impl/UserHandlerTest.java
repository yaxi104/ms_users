package com.hexagonal.ms_user.application.handler.impl;

import com.hexagonal.ms_user.application.dto.request.UserRequest;
import com.hexagonal.ms_user.application.mapper.IUserRequestMapper;
import com.hexagonal.ms_user.domain.api.IUserServicePort;
import com.hexagonal.ms_user.infrastructure.exception.UserNotOlderAgeException;
import com.hexagonal.ms_user.util.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserHandlerTest {
    @InjectMocks
    private UserHandler userHandler;

    @Mock
    private IUserServicePort userServicePort;

    @Mock
    private IUserRequestMapper userRequestMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void saveUserSuccessTest() {
        var mockUserRequest = TestDataFactory.mockUserRequest();
        var mockUser = TestDataFactory.mockUser();

        when(userRequestMapper.toUser(mockUserRequest)).thenReturn(mockUser);
        when(passwordEncoder.encode("password123")).thenReturn("encodePassword");

        userHandler.saveUser(mockUserRequest);

        assertEquals("encodePassword", mockUser.getPassword());
        assertEquals("PROPIETARIO", mockUser.getRole());

        verify(userRequestMapper).toUser(mockUserRequest);
        verify(passwordEncoder).encode("password123");
        verify(userServicePort).saveUser(mockUser);

    }

    @Test
    void saveUserThrowsUserNotOlderAgeExceptionTest() {
        UserRequest request = new UserRequest();
        request.setDateBirth(LocalDate.of(9999, 12, 31));

        UserNotOlderAgeException ex = assertThrows(UserNotOlderAgeException.class, () -> {
            userHandler.saveUser(request);
        });

        assertNotNull(ex);
        verifyNoInteractions(userRequestMapper, passwordEncoder, userServicePort);
    }

}