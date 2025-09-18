package com.hexagonal.ms_user.infrastructure.output.jpa.adapter;

import com.hexagonal.ms_user.domain.model.User;
import com.hexagonal.ms_user.domain.spi.IUserPersistencePort;
import com.hexagonal.ms_user.infrastructure.exception.UserAlreadyExistsException;
import com.hexagonal.ms_user.infrastructure.output.jpa.mapper.IUserEntityMapper;
import com.hexagonal.ms_user.infrastructure.output.jpa.repository.IUserRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserJpaAdapter implements IUserPersistencePort {

    private final IUserRepository userRepository;

    private final IUserEntityMapper userEntityMapper;

    @Override
    public void saveUser(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException();
        }
        userRepository.save(userEntityMapper.toEntity(user));
    }
}
