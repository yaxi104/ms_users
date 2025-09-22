package com.hexagonal.ms_user.application.handler.impl;

import com.hexagonal.ms_user.application.dto.request.AuthRequest;
import com.hexagonal.ms_user.application.dto.request.UserEmployeeRequest;
import com.hexagonal.ms_user.application.dto.request.UserOwnerRequest;
import com.hexagonal.ms_user.application.dto.response.UserAuthResponse;
import com.hexagonal.ms_user.application.dto.response.UserResponse;
import com.hexagonal.ms_user.application.mapper.IAuthMapper;
import com.hexagonal.ms_user.application.mapper.IEmployeeMapper;
import com.hexagonal.ms_user.application.mapper.IOwnerMapper;
import com.hexagonal.ms_user.application.mapper.IUserMapper;
import com.hexagonal.ms_user.domain.api.IUserServicePort;
import com.hexagonal.ms_user.domain.model.request.User;
import com.hexagonal.ms_user.domain.model.response.UserAuth;
import com.hexagonal.ms_user.util.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserHandlerTest {
    @InjectMocks
    private UserHandler userHandler;

    @Mock
    private IUserServicePort userServicePort;

    @Mock
    private IOwnerMapper ownerMapper;

    @Mock
    private IAuthMapper authMapper;

    @Mock
    private IUserMapper userMapper;

    @Mock
    private IEmployeeMapper employeeMapper;

    @Test
    void authUserSuccessTest() {
        AuthRequest mockAuthRequest = TestDataFactory.mockAuthequest();
        userHandler.authUser(mockAuthRequest);

        verify(authMapper).toUser(mockAuthRequest);
    }

    @Test
    void saveOwnerSuccessTest() {
        UserOwnerRequest userOwnerRequest = TestDataFactory.mockOwnerRequest();
        userHandler.saveOwner(userOwnerRequest);

        verify(ownerMapper).toOwner(userOwnerRequest);
    }

    @Test
    void getUserByEmailSuccesTest() {
        String email = "test@example.com";
        User mockUser = TestDataFactory.mockUser();
        UserResponse mockResponse = TestDataFactory.mockUserResponse();

        when(userServicePort.getUserByEmail(email)).thenReturn(mockUser);
        when(userMapper.toResponse(mockUser)).thenReturn(mockResponse);

        UserResponse result = userHandler.getUserByEmail(email);

        assertNotNull(result);
        assertEquals(mockResponse, result);
        verify(userServicePort).getUserByEmail(email);
        verify(userMapper).toResponse(mockUser);
    }

    @Test
    void getUserByIdSuccessTest() {
        Long userId = 1L;
        User mockUser = TestDataFactory.mockUser();
        UserResponse mockResponse = TestDataFactory.mockUserResponse();

        when(userServicePort.getUserById(userId)).thenReturn(mockUser);
        when(userMapper.toResponse(mockUser)).thenReturn(mockResponse);

        UserResponse result = userHandler.getUserById(userId);

        assertNotNull(result);
        assertEquals(mockResponse, result);
        verify(userServicePort).getUserById(userId);
        verify(userMapper).toResponse(mockUser);
    }

    @Test
    void getUserByIdAuthSuccessTest() {
        Long userId = 1L;
        UserAuthResponse mockUser = new UserAuthResponse();
        UserAuth userAuth = new UserAuth();

        when(userServicePort.getUserByIdAuth(userId)).thenReturn(userAuth);
        when(userMapper.toUserAuthReponse(userAuth)).thenReturn(mockUser);

        UserAuthResponse result = userHandler.getUserByIdAuth(userId);

        assertNotNull(result);
        verify(userServicePort).getUserByIdAuth(userId);
        verify(userMapper).toUserAuthReponse(userAuth);
    }

    @Test
    void saveEmployeeSuccessTest() {
        UserEmployeeRequest userEmployeeRequest = new UserEmployeeRequest();
        userHandler.saveEmployee(userEmployeeRequest);

        verify(employeeMapper).toEmployee(userEmployeeRequest);
    }

}