package com.hexagonal.ms_user.infrastructure.configuration;

import com.hexagonal.ms_user.domain.api.IUserServicePort;
import com.hexagonal.ms_user.domain.spi.IAuthPersistencePort;
import com.hexagonal.ms_user.domain.spi.IAuthTokenResponsePort;
import com.hexagonal.ms_user.domain.spi.IPasswordEncodePort;
import com.hexagonal.ms_user.domain.spi.IUserPersistencePort;
import com.hexagonal.ms_user.domain.usecase.UserUseCase;
import com.hexagonal.ms_user.infrastructure.output.jpa.adapter.AuthJpaAdapter;
import com.hexagonal.ms_user.infrastructure.output.jpa.adapter.UserJpaAdapter;
import com.hexagonal.ms_user.infrastructure.output.jpa.mapper.IUserEntityMapper;
import com.hexagonal.ms_user.infrastructure.output.jpa.repository.IUserRepository;
import com.hexagonal.ms_user.infrastructure.security.AuthTokenResponse;
import com.hexagonal.ms_user.infrastructure.security.jwt.JwtService;
import com.hexagonal.ms_user.infrastructure.security.PasswordEncodeService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class BeanConfiguration {

    @Bean
    public IUserPersistencePort userPersistencePort(IUserRepository userRepository, IUserEntityMapper userEntityMapper) {
        return new UserJpaAdapter(userRepository, userEntityMapper);
    }

    @Bean
    public IAuthPersistencePort authPersistencePort(IUserRepository userRepository, IUserEntityMapper userEntityMapper) {
        return new AuthJpaAdapter(userRepository, userEntityMapper);
    }

    @Bean
    public IPasswordEncodePort passwordEncodePort(PasswordEncoder passwordEncoder) {
        return new PasswordEncodeService(passwordEncoder);
    }

    @Bean
    public IAuthTokenResponsePort authTokenResponsePort(
            AuthenticationManager authManager,
            JwtService jwtService,
            IAuthPersistencePort authPersistencePort
    ) {
        return new AuthTokenResponse(authManager, jwtService, authPersistencePort);
    }

    @Bean
    public IUserServicePort userServicePort(
            IUserPersistencePort userPersistencePort,
            IAuthPersistencePort authPersistencePort,
            IPasswordEncodePort passwordEncodePort,
            IAuthTokenResponsePort authTokenResponsePort
    ) {
        return new UserUseCase(userPersistencePort, authPersistencePort, passwordEncodePort, authTokenResponsePort);
    }
}
