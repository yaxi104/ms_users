package com.hexagonal.ms_user.infrastructure.security.jwt;

import com.hexagonal.ms_user.domain.model.request.User;
import com.hexagonal.ms_user.util.TestDataFactory;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class JwtServiceTest {

    private JwtService jwtService;
    private final long tokenValidity = 1000 * 60 * 60;

    @BeforeEach
    void setUp() {
        String secret = "MySuperSecretKeyForJwtWhichNeedsToBeLongEnough12345";
        jwtService = new JwtService(secret, tokenValidity);
    }

    @Test
    void generateTokenContainsClaimsAndSubject() {
        User user = TestDataFactory.mockUser();
        user.setRole("ROLE_" + user.getRole());
        String token = jwtService.generateToken(user);
        assertNotNull(token);

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor("MySuperSecretKeyForJwtWhichNeedsToBeLongEnough12345".getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody();

        assertEquals("test@example.com", claims.getSubject());
        assertEquals("ROLE_PROPIETARIO", claims.get("role"));
        assertEquals(1L, claims.get("id", Long.class));
    }

    @Test
    void extractUsernameReturnsCorrectUsername() {
        User user = TestDataFactory.mockUser();

        String token = jwtService.generateToken(user);

        String username = jwtService.extractUsername(token);
        assertEquals("test@example.com", username);
    }

    @Test
    void isTokenValidReturnsTrueForValidToken() {
        User user = TestDataFactory.mockUser();

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("test@example.com");

        String token = jwtService.generateToken(user);

        boolean isValid = jwtService.isTokenValid(token, userDetails);
        assertTrue(isValid);
    }

    @Test
    void isTokenValidReturnsFalseIfUsernameDoesNotMatch() {
        User user = TestDataFactory.mockUser();

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("differentuser@example.com");

        String token = jwtService.generateToken(user);

        boolean isValid = jwtService.isTokenValid(token, userDetails);
        assertFalse(isValid);
    }

    @Test
    void extractUsernameThrowsExceptionForInvalidToken() {
        String invalidToken = "invalid.token.here";

        assertThrows(io.jsonwebtoken.JwtException.class, () -> {
            jwtService.extractUsername(invalidToken);
        });
    }
}