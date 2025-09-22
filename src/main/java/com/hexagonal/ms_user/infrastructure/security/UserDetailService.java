package com.hexagonal.ms_user.infrastructure.security;

import com.hexagonal.ms_user.domain.spi.IAuthPersistencePort;
import com.hexagonal.ms_user.domain.spi.IRolePersistencePort;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailService implements UserDetailsService {

    private final IAuthPersistencePort authPersistencePort;
    private final IRolePersistencePort rolePersistencePort;

    public UserDetailService(IAuthPersistencePort authPersistencePort, IRolePersistencePort rolePersistencePort) {
        this.authPersistencePort = authPersistencePort;
        this.rolePersistencePort = rolePersistencePort;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        var user = authPersistencePort.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        var role = rolePersistencePort.getRolById(user.getRoleId()).orElseThrow(() -> new UsernameNotFoundException("Role not found"));

        return new User(
                user.getEmail(),
                user.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_" + role.getName()))
        );
    }
}