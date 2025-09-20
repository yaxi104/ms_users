package com.hexagonal.ms_user.application.handler;

import com.hexagonal.ms_user.application.dto.request.AuthRequest;
import com.hexagonal.ms_user.application.dto.request.UserOwnerRequest;
import com.hexagonal.ms_user.application.dto.response.AuthResponse;
import com.hexagonal.ms_user.application.dto.response.UserResponse;

public interface IUserHandler {

    AuthResponse authUser(AuthRequest authRequest);

    void saveOwner(UserOwnerRequest userOwnerRequest);

    UserResponse getUserByEmail(String email);

    UserResponse getUserById(Long id);
}
