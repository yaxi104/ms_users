package com.hexagonal.ms_user.application.handler.impl;

import com.hexagonal.ms_user.application.dto.request.AuthRequest;
import com.hexagonal.ms_user.application.dto.request.UserOwnerRequest;
import com.hexagonal.ms_user.application.mapper.IAuthMapper;
import com.hexagonal.ms_user.application.mapper.IOwnerMapper;
import com.hexagonal.ms_user.application.mapper.IUserMapper;
import com.hexagonal.ms_user.domain.api.IUserServicePort;
import com.hexagonal.ms_user.util.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

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

}