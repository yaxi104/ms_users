package com.hexagonal.ms_user.infrastructure.configuration;

import com.hexagonal.ms_user.domain.api.IUserServicePort;
import com.hexagonal.ms_user.domain.spi.IUserPersistencePort;
import com.hexagonal.ms_user.domain.usecase.UserUseCase;
import com.hexagonal.ms_user.infrastructure.output.jpa.adapter.UserJpaAdapter;
import com.hexagonal.ms_user.infrastructure.output.jpa.mapper.IUserEntityMapper;
import com.hexagonal.ms_user.infrastructure.output.jpa.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class BeanConfiguration {

    private final IUserRepository userRepository;
    private final IUserEntityMapper userEntityMapper;

    @Bean
    public IUserPersistencePort userPersistencePort(){
        return new UserJpaAdapter(userRepository, userEntityMapper);
    }

    @Bean
    public IUserServicePort userServicePort(){
        return new UserUseCase(userPersistencePort());
    }

}
