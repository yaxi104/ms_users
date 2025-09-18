package com.hexagonal.ms_user.domain.spi;

import com.hexagonal.ms_user.domain.model.User;

import java.util.Optional;

public interface IUserPersistencePort {

    void saveUser(User user);

    Optional<User> findByEmail(String email);
}
