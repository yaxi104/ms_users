package com.hexagonal.ms_user.infrastructure.security;

import com.hexagonal.ms_user.domain.model.request.User;
import com.hexagonal.ms_user.domain.spi.IAuthPersistencePort;
import com.hexagonal.ms_user.domain.spi.IRolePersistencePort;
import com.hexagonal.ms_user.util.TestDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserDetailServiceTest {

    private IAuthPersistencePort authPersistencePort;
    private IRolePersistencePort rolePersistencePort;

    private UserDetailService userDetailsService;

    @BeforeEach
    void setUp() {
        authPersistencePort = mock(IAuthPersistencePort.class);
        rolePersistencePort = mock(IRolePersistencePort.class);
        userDetailsService = new UserDetailService(authPersistencePort, rolePersistencePort);
    }

    @Test
    void loadUserByUsernameUserFoundTest() {
        var mockUser = new User();
        mockUser.setEmail("test@example.com");
        mockUser.setPassword("password123");

        when(authPersistencePort.findByEmail("test@example.com")).thenReturn(Optional.of(mockUser));
        when(rolePersistencePort.getRolById(mockUser.getRoleId())).thenReturn(Optional.of(TestDataFactory.mockRole()));

        UserDetails userDetails = userDetailsService.loadUserByUsername("test@example.com");

        assertNotNull(userDetails);
        assertEquals("test@example.com", userDetails.getUsername());
        assertEquals("password123", userDetails.getPassword());
    }

    @Test
    void loadUserByUsernameUserNotFoundTest() {
        when(authPersistencePort.findByEmail("noexiste@example.com")).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername("noexiste@example.com");
        });
    }
}