package com.hexagonal.ms_user.infrastructure.output.jpa.adapter;

import com.hexagonal.ms_user.domain.model.request.User;
import com.hexagonal.ms_user.domain.spi.IAuthPersistencePort;
import com.hexagonal.ms_user.infrastructure.output.jpa.mapper.IUserEntityMapper;
import com.hexagonal.ms_user.infrastructure.output.jpa.repository.IUserRepository;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class AuthJpaAdapter implements IAuthPersistencePort {

    private final IUserRepository userRepository;

    private final IUserEntityMapper userEntityMapper;

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(userEntityMapper::toUser);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id)
                .map(userEntityMapper::toUser);
    }

}
