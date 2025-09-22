package com.hexagonal.ms_user.infrastructure.security.jwt;

import com.hexagonal.ms_user.domain.exception.RoleNotFoundException;
import com.hexagonal.ms_user.domain.model.request.Role;
import com.hexagonal.ms_user.domain.model.request.User;
import com.hexagonal.ms_user.domain.spi.IRolePersistencePort;
import com.hexagonal.ms_user.util.TestDataFactory;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.SecretKey;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class JwtServiceTest {


    private JwtService jwtService;
    private SecretKey secretKey;
    private final long tokenValidity = 1000 * 60 * 60;
    private IRolePersistencePort rolePersistencePort;
    private String secret;

    @BeforeEach
    void setUp() {
        secret = "MySuperSecretKeyForJwtWhichNeedsToBeLongEnough12345";
        secretKey = Keys.hmacShaKeyFor(secret.getBytes());
        rolePersistencePort = mock(IRolePersistencePort.class);
        jwtService = new JwtService(secret, tokenValidity, rolePersistencePort);
    }

    @Test
    void generateTokenContainsClaimsAndSubject() {
        User user = TestDataFactory.mockUser();
        Role role = TestDataFactory.mockRole();
        role.setName("ROLE_PROPIETARIO");
        when(rolePersistencePort.getRolById(user.getRoleId())).thenReturn(Optional.of(role));

        String token = jwtService.generateToken(user);

        assertNotNull(token);

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        assertEquals(user.getEmail(), claims.getSubject());
        assertEquals("ROLE_PROPIETARIO", claims.get("role"));
        assertEquals(user.getId(), claims.get("id", Long.class));
    }

    @Test
    void extractUsernameReturnsCorrectUsername() {
        User user = TestDataFactory.mockUser();
        Role role = TestDataFactory.mockRole();
        role.setName("ROLE_PROPIETARIO");

        when(rolePersistencePort.getRolById(user.getRoleId())).thenReturn(Optional.of(role));

        String token = jwtService.generateToken(user);
        String username = jwtService.extractUsername(token);

        assertEquals(user.getEmail(), username);
    }

    @Test
    void isTokenValidReturnsTrueForValidToken() {
        User user = TestDataFactory.mockUser();
        Role role = TestDataFactory.mockRole();
        role.setName("ROLE_PROPIETARIO");

        when(rolePersistencePort.getRolById(user.getRoleId())).thenReturn(Optional.of(role));

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn(user.getEmail());

        String token = jwtService.generateToken(user);
        boolean isValid = jwtService.isTokenValid(token, userDetails);

        assertTrue(isValid);
    }

    @Test
    void isTokenValidReturnsFalseIfUsernameDoesNotMatch() {
        User user = TestDataFactory.mockUser();
        Role role = TestDataFactory.mockRole();
        role.setName("ROLE_PROPIETARIO");

        when(rolePersistencePort.getRolById(user.getRoleId())).thenReturn(Optional.of(role));

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("differentuser@example.com");

        String token = jwtService.generateToken(user);
        boolean isValid = jwtService.isTokenValid(token, userDetails);

        assertFalse(isValid);
    }

    @Test
    void extractUsernameThrowsExceptionForInvalidToken() {
        String invalidToken = "invalid.token.here";

        assertThrows(Exception.class, () -> jwtService.extractUsername(invalidToken));
    }

    @Test
    void generateTokenThrowsExceptionWhenRoleNotFound() {
        User user = TestDataFactory.mockUser();

        when(rolePersistencePort.getRolById(user.getRoleId())).thenReturn(Optional.empty());

        assertThrows(RoleNotFoundException.class, () -> jwtService.generateToken(user));
    }
}
