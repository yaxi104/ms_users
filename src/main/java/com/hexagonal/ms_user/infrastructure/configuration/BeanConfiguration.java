package com.hexagonal.ms_user.infrastructure.configuration;

import com.hexagonal.ms_user.domain.api.IRoleServicePort;
import com.hexagonal.ms_user.domain.api.IUserServicePort;
import com.hexagonal.ms_user.domain.spi.IAuthPersistencePort;
import com.hexagonal.ms_user.domain.spi.IAuthTokenResponsePort;
import com.hexagonal.ms_user.domain.spi.IPasswordEncodePort;
import com.hexagonal.ms_user.domain.spi.IRolePersistencePort;
import com.hexagonal.ms_user.domain.spi.IUserPersistencePort;
import com.hexagonal.ms_user.domain.usecase.RoleUseCase;
import com.hexagonal.ms_user.domain.usecase.UserUseCase;
import com.hexagonal.ms_user.infrastructure.output.jpa.role.adapter.RoleJpaAdapter;
import com.hexagonal.ms_user.infrastructure.output.jpa.role.mapper.IRoleEntityMapper;
import com.hexagonal.ms_user.infrastructure.output.jpa.role.repository.IRoleRepository;
import com.hexagonal.ms_user.infrastructure.output.jpa.user.adapter.AuthJpaAdapter;
import com.hexagonal.ms_user.infrastructure.output.jpa.user.adapter.UserJpaAdapter;
import com.hexagonal.ms_user.infrastructure.output.jpa.user.mapper.IUserEntityMapper;
import com.hexagonal.ms_user.infrastructure.output.jpa.user.repository.IUserRepository;
import com.hexagonal.ms_user.infrastructure.security.AuthTokenResponse;
import com.hexagonal.ms_user.infrastructure.security.PasswordEncodeService;
import com.hexagonal.ms_user.infrastructure.security.jwt.JwtService;
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
    public IRolePersistencePort rolePersistencePort(IRoleRepository roleRepository, IRoleEntityMapper roleEntityMapper) {
        return new RoleJpaAdapter(roleRepository, roleEntityMapper);
    }


    @Bean
    public IUserServicePort userServicePort(
            IUserPersistencePort userPersistencePort,
            IAuthPersistencePort authPersistencePort,
            IPasswordEncodePort passwordEncodePort,
            IAuthTokenResponsePort authTokenResponsePort,
            IRolePersistencePort rolePersistencePort
    ) {
        return new UserUseCase(userPersistencePort, authPersistencePort, passwordEncodePort, authTokenResponsePort, rolePersistencePort);
    }

    @Bean
    public IRoleServicePort roleServicePort(
            IRolePersistencePort rolePersistencePort
    ) {
        return new RoleUseCase(rolePersistencePort);
    }
}
