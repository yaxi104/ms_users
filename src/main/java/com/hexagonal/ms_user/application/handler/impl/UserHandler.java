package com.hexagonal.ms_user.application.handler.impl;

import com.hexagonal.ms_user.application.dto.request.AuthRequest;
import com.hexagonal.ms_user.application.dto.request.UserOwnerRequest;
import com.hexagonal.ms_user.application.dto.response.AuthResponse;
import com.hexagonal.ms_user.application.dto.response.UserResponse;
import com.hexagonal.ms_user.application.handler.IUserHandler;
import com.hexagonal.ms_user.application.mapper.IAuthMapper;
import com.hexagonal.ms_user.application.mapper.IOwnerMapper;
import com.hexagonal.ms_user.application.mapper.IUserMapper;
import com.hexagonal.ms_user.domain.api.IUserServicePort;
import com.hexagonal.ms_user.domain.model.response.TokenResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class UserHandler implements IUserHandler {

    private final IUserServicePort userServicePort;
    private final IOwnerMapper ownerMapper;
    private final IAuthMapper authMapper;
    private final IUserMapper userMapper;

    @Override
    public AuthResponse authUser(AuthRequest authRequest) {
        TokenResponse tokenResponse = userServicePort.authUser(authMapper.toUser(authRequest));
        return authMapper.toAuthResponse(tokenResponse);
    }

    @Override
    public void saveOwner(UserOwnerRequest userOwnerRequest) {
        userServicePort.saveUser(ownerMapper.toOwner(userOwnerRequest));
    }

    @Override
    public UserResponse getUserByEmail(String email) {
        return userMapper.toResponse(userServicePort.getUserByEmail(email));
    }
}
