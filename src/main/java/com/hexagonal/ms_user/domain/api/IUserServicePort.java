package com.hexagonal.ms_user.domain.api;

import com.hexagonal.ms_user.domain.model.User;

public interface IUserServicePort {

    void saveUser(User user);
}
