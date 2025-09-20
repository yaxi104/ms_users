package com.hexagonal.ms_user.domain.spi;

import com.hexagonal.ms_user.domain.model.request.User;

import java.util.Optional;

public interface IAuthPersistencePort {

    Optional<User> findByEmail(String email);
}
