package com.hexagonal.ms_user.infrastructure.security;

import com.hexagonal.ms_user.domain.model.request.User;
import com.hexagonal.ms_user.domain.model.response.TokenResponse;
import com.hexagonal.ms_user.domain.spi.IAuthPersistencePort;
import com.hexagonal.ms_user.domain.spi.IAuthTokenResponsePort;
import com.hexagonal.ms_user.infrastructure.exception.UserForbiddenException;
import com.hexagonal.ms_user.infrastructure.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthTokenResponse implements IAuthTokenResponsePort {

    private final AuthenticationManager authManager;
    private final JwtService jwtService;
    private final IAuthPersistencePort authPersistencePort;

    @Override
    public TokenResponse getToken(User user) {
        try {
            Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword())
            );

            String email = authentication.getName();

            User userDb = authPersistencePort.findByEmail(email)
                    .orElseThrow(UserForbiddenException::new);

            String token = jwtService.generateToken(userDb);

            return new TokenResponse(token);

        } catch (BadCredentialsException e) {
            throw new UserForbiddenException();
        }
    }
}