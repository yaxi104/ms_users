package com.hexagonal.ms_user.infrastructure.security;

import com.hexagonal.ms_user.domain.spi.IPasswordEncodePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PasswordEncodeServiceTest {

    private PasswordEncoder passwordEncoder;
    private IPasswordEncodePort passwordEncodeService;

    @BeforeEach
    void setUp() {
        passwordEncoder = mock(PasswordEncoder.class);
        passwordEncodeService = new PasswordEncodeService(passwordEncoder);
    }

    @Test
    void encodePasswordReturnsEncodedPassword() {
        String rawPassword = "myPassword123";
        String encodedPassword = "$2a$10$abcdef...";

        when(passwordEncoder.encode(rawPassword)).thenReturn(encodedPassword);

        String result = passwordEncodeService.encodePassword(rawPassword);

        assertEquals(encodedPassword, result);
    }
}