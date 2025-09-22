package com.hexagonal.ms_user.domain.api;

import com.hexagonal.ms_user.domain.model.request.User;
import com.hexagonal.ms_user.domain.model.response.TokenResponse;
import com.hexagonal.ms_user.domain.model.response.UserAuth;

public interface IUserServicePort {

    TokenResponse authUser(User user);

    void saveOwner(User user);

    User getUserByEmail(String email);

    User getUserById(Long id);

    UserAuth getUserByIdAuth(Long id);

    void saveEmployee(User user);
}
