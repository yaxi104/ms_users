package com.hexagonal.ms_user.infrastructure.security;

import com.hexagonal.ms_user.domain.model.request.User;
import com.hexagonal.ms_user.domain.model.response.TokenResponse;
import com.hexagonal.ms_user.domain.spi.IAuthPersistencePort;
import com.hexagonal.ms_user.infrastructure.exception.UserForbiddenException;
import com.hexagonal.ms_user.infrastructure.security.jwt.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AuthTokenResponseTest {

    private AuthenticationManager authenticationManager;
    private JwtService jwtService;
    private IAuthPersistencePort authPersistencePort;
    private AuthTokenResponse authTokenResponse;

    @BeforeEach
    void setUp() {
        authenticationManager = mock(AuthenticationManager.class);
        jwtService = mock(JwtService.class);
        authPersistencePort = mock(IAuthPersistencePort.class);
        authTokenResponse = new AuthTokenResponse(authenticationManager, jwtService, authPersistencePort);
    }

    @Test
    void getTokenReturnsTokenResponseWhenCredentialsAreValid() {
        User inputUser = new User();
        inputUser.setEmail("test@example.com");
        inputUser.setPassword("password");

        User userFromDb = new User();
        userFromDb.setEmail("test@example.com");

        Authentication authentication = mock(Authentication.class);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.getName()).thenReturn("test@example.com");
        when(authPersistencePort.findByEmail("test@example.com")).thenReturn(Optional.of(userFromDb));
        when(jwtService.generateToken(userFromDb)).thenReturn("mocked-token");

        TokenResponse tokenResponse = authTokenResponse.getToken(inputUser);

        assertNotNull(tokenResponse);
        assertEquals("mocked-token", tokenResponse.getToken());
    }

    @Test
    void getTokenThrowsUserForbiddenExceptionWhenCredentialsAreInvalid() {
        User inputUser = new User();
        inputUser.setEmail("invalid@example.com");
        inputUser.setPassword("wrongPassword");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        assertThrows(UserForbiddenException.class, () -> authTokenResponse.getToken(inputUser));
    }

    @Test
    void getTokenThrowsUserForbiddenExceptionWhenUserNotFoundInDatabase() {
        User inputUser = new User();
        inputUser.setEmail("test@example.com");
        inputUser.setPassword("password");

        Authentication authentication = mock(Authentication.class);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.getName()).thenReturn("test@example.com");
        when(authPersistencePort.findByEmail("test@example.com")).thenReturn(Optional.empty());

        assertThrows(UserForbiddenException.class, () -> authTokenResponse.getToken(inputUser));
    }
}