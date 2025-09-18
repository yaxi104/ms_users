package com.hexagonal.ms_user.domain.spi;

import com.hexagonal.ms_user.domain.model.User;

public interface IUserPersistencePort {

    void saveUser(User user);

}
