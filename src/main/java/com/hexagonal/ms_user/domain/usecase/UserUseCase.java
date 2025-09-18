package com.hexagonal.ms_user.domain.usecase;

import com.hexagonal.ms_user.domain.api.IUserServicePort;
import com.hexagonal.ms_user.domain.model.User;
import com.hexagonal.ms_user.domain.spi.IUserPersistencePort;

public class UserUseCase implements IUserServicePort {

    private final IUserPersistencePort userPersistencePort;

    public UserUseCase(IUserPersistencePort userPersistencePort) {
        this.userPersistencePort = userPersistencePort;
    }

    @Override
    public void saveUser(User user) {
        userPersistencePort.saveUser(user);
    }
}
