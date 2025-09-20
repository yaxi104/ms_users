package com.hexagonal.ms_user.domain.api;

import com.hexagonal.ms_user.domain.model.request.User;
import com.hexagonal.ms_user.domain.model.response.TokenResponse;

public interface IUserServicePort {

    TokenResponse authUser(User user);

    void saveUser(User user);

    User getUserByEmail(String email);

    User getUserById(Long id);
}
