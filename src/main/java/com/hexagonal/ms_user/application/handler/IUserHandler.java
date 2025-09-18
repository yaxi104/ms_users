package com.hexagonal.ms_user.application.handler;

import com.hexagonal.ms_user.application.dto.request.UserRequest;

public interface IUserHandler {

    void saveUser(UserRequest userRequest);
}
