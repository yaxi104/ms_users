package com.hexagonal.ms_user.infrastructure.security;

import com.hexagonal.ms_user.infrastructure.output.jpa.entity.UserEntity;
import com.hexagonal.ms_user.infrastructure.output.jpa.repository.IUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserDetailServiceAdapterTest {

    private IUserRepository userRepository;
    private UserDetailServiceAdapter userDetailsService;

    @BeforeEach
    void setUp() {
        userRepository = mock(IUserRepository.class);
        userDetailsService = new UserDetailServiceAdapter(userRepository);
    }

    @Test
    void loadUserByUsernameUserFoundTest() {
        var mockUser = new UserEntity();
        mockUser.setEmail("test@example.com");
        mockUser.setPassword("password123");
        mockUser.setRole("USER");

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(mockUser));

        UserDetails userDetails = userDetailsService.loadUserByUsername("test@example.com");

        assertNotNull(userDetails);
        assertEquals("test@example.com", userDetails.getUsername());
        assertEquals("password123", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_USER")));
    }

    @Test
    void loadUserByUsernameUserNotFoundTest() {
        when(userRepository.findByEmail("noexiste@example.com")).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername("noexiste@example.com");
        });
    }
}