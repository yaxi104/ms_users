package com.hexagonal.ms_user.infrastructure.output.jpa.user.adapter;

import com.hexagonal.ms_user.domain.model.request.User;
import com.hexagonal.ms_user.domain.spi.IUserPersistencePort;
import com.hexagonal.ms_user.infrastructure.output.jpa.user.mapper.IUserEntityMapper;
import com.hexagonal.ms_user.infrastructure.output.jpa.user.repository.IUserRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserJpaAdapter implements IUserPersistencePort {

    private final IUserRepository userRepository;

    private final IUserEntityMapper userEntityMapper;

    @Override
    public void saveUser(User user) {
        userRepository.save(userEntityMapper.toEntity(user));
    }

}
