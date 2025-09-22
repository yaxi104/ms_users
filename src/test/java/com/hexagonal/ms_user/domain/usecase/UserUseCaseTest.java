package com.hexagonal.ms_user.domain.usecase;

import com.hexagonal.ms_user.domain.exception.BadRequestException;
import com.hexagonal.ms_user.domain.exception.RoleNotFoundException;
import com.hexagonal.ms_user.domain.exception.UserAlreadyExistsException;
import com.hexagonal.ms_user.domain.exception.UserNotFoundException;
import com.hexagonal.ms_user.domain.model.request.Role;
import com.hexagonal.ms_user.domain.model.request.User;
import com.hexagonal.ms_user.domain.model.response.TokenResponse;
import com.hexagonal.ms_user.domain.model.response.UserAuth;
import com.hexagonal.ms_user.domain.spi.IAuthPersistencePort;
import com.hexagonal.ms_user.domain.spi.IAuthTokenResponsePort;
import com.hexagonal.ms_user.domain.spi.IPasswordEncodePort;
import com.hexagonal.ms_user.domain.spi.IRolePersistencePort;
import com.hexagonal.ms_user.domain.spi.IUserPersistencePort;
import com.hexagonal.ms_user.util.TestDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static com.hexagonal.ms_user.domain.utils.Constanst.EMPLEADO;
import static com.hexagonal.ms_user.domain.utils.Constanst.PROPIETARIO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserUseCaseTest {

    private IUserPersistencePort userPersistencePort;
    private IAuthPersistencePort authPersistencePort;
    private IPasswordEncodePort passwordEncodePort;
    private IAuthTokenResponsePort authTokenResponsePort;
    private IRolePersistencePort rolePersistencePort;


    private UserUseCase userUseCase;

    @BeforeEach
    void setUp() {
        userPersistencePort = mock(IUserPersistencePort.class);
        authPersistencePort = mock(IAuthPersistencePort.class);
        passwordEncodePort = mock(IPasswordEncodePort.class);
        authTokenResponsePort = mock(IAuthTokenResponsePort.class);
        rolePersistencePort = mock(IRolePersistencePort.class);
        userUseCase = new UserUseCase(
                userPersistencePort,
                authPersistencePort,
                passwordEncodePort,
                authTokenResponsePort,
                rolePersistencePort
        );
    }

    @Test
    void saveUserSuccessTest() {
        User user = TestDataFactory.mockUser();
        when(rolePersistencePort.getRolByName(PROPIETARIO)).thenReturn(Optional.of(TestDataFactory.mockRole()));
        when(authPersistencePort.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        when(passwordEncodePort.encodePassword(user.getPassword())).thenReturn("encodedPassword");

        userUseCase.saveOwner(user);

        verify(userPersistencePort, times(1)).saveUser(user);
        assertEquals("encodedPassword", user.getPassword());
    }

    @Test
    void saveUserFailExistsTest() {
        User user = TestDataFactory.mockUser();
        when(rolePersistencePort.getRolByName(PROPIETARIO)).thenReturn(Optional.of(TestDataFactory.mockRole()));
        when(authPersistencePort.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        assertThrows(UserAlreadyExistsException.class, () -> userUseCase.saveOwner(user));
        verify(userPersistencePort, Mockito.never()).saveUser(any());
    }

    @Test
    void saveUserFailInvalidEmailTest() {
        User user = TestDataFactory.mockUser();
        user.setEmail("correo-invalido");
        when(rolePersistencePort.getRolByName(PROPIETARIO)).thenReturn(Optional.of(TestDataFactory.mockRole()));

        assertThrows(BadRequestException.class, () -> userUseCase.saveOwner(user));
    }

    @Test
    void saveUserFailFutureBirthDateTest() {
        User user = TestDataFactory.mockUser();
        user.setDateBirth(LocalDate.now().plusDays(5));
        when(rolePersistencePort.getRolByName(PROPIETARIO)).thenReturn(Optional.of(TestDataFactory.mockRole()));

        assertThrows(BadRequestException.class, () -> userUseCase.saveOwner(user));
    }

    @Test
    void saveUserFailPhonePatternTest() {
        User user = TestDataFactory.mockUser();
        user.setPhoneNumber("abc123");
        when(rolePersistencePort.getRolByName(PROPIETARIO)).thenReturn(Optional.of(TestDataFactory.mockRole()));

        assertThrows(BadRequestException.class, () -> userUseCase.saveOwner(user));
    }

    @Test
    void authUserSuccessTest() {
        User user = TestDataFactory.mockUser();
        TokenResponse expected = new TokenResponse("jwt.token");
        when(authTokenResponsePort.getToken(user)).thenReturn(expected);

        TokenResponse actual = userUseCase.authUser(user);

        assertEquals(expected.getToken(), actual.getToken());
    }

    @Test
    void authUserFailMissingEmailTest() {
        User user = TestDataFactory.mockUser();
        user.setEmail(null);

        assertThrows(BadRequestException.class, () -> userUseCase.authUser(user));
    }

    @Test
    void authUserFailMissingPasswordTest() {
        User user = TestDataFactory.mockUser();
        user.setPassword("   ");

        assertThrows(BadRequestException.class, () -> userUseCase.authUser(user));
    }

    @Test
    void getUserByEmailSuccesTest() {
        String email = "test@example.com";
        User mockUser = TestDataFactory.mockUser();

        when(authPersistencePort.findByEmail(email)).thenReturn(Optional.of(mockUser));

        User result = userUseCase.getUserByEmail(email);

        assertNotNull(result);
        assertEquals(email, result.getEmail());
        verify(authPersistencePort).findByEmail(email);
    }

    @Test
    void getUserByEmailNotExistTest() {
        String email = "notfound@example.com";
        when(authPersistencePort.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userUseCase.getUserByEmail(email));
        verify(authPersistencePort).findByEmail(email);
    }

    @Test
    void getUserByIdSuccessTest() {
        Long userId = 1L;
        User mockUser = TestDataFactory.mockUser();

        when(authPersistencePort.findById(userId)).thenReturn(Optional.of(mockUser));

        User result = userUseCase.getUserById(userId);

        assertNotNull(result);
        assertEquals(userId, result.getId());
        verify(authPersistencePort).findById(userId);
    }

    @Test
    void getUserByIdWhenUserDoesNotExist() {
        Long userId = 999L;
        when(authPersistencePort.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userUseCase.getUserById(userId));
        verify(authPersistencePort).findById(userId);
    }

    @Test
    void getUserByIdAuthSucess() {
        Long userId = 1L;
        User user = TestDataFactory.mockUser();
        Role role = TestDataFactory.mockRole();
        when(authPersistencePort.findById(userId)).thenReturn(Optional.of(user));
        when(rolePersistencePort.getRolById(user.getRoleId())).thenReturn(Optional.of(role));

        UserAuth result = userUseCase.getUserByIdAuth(userId);

        assertNotNull(result);
        assertEquals(userId, result.getId());
        verify(authPersistencePort).findById(userId);
    }

    @Test
    void getUserByIdAuthWhenUserDoesNotExist() {
        Long userId = 999L;
        when(authPersistencePort.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userUseCase.getUserByIdAuth(userId));
        verify(authPersistencePort).findById(userId);
    }

    @Test
    void getUserByIdAuthRoleWhenUserDoesNotExist() {
        Long userId = 999L;
        User user = TestDataFactory.mockUser();
        when(authPersistencePort.findById(userId)).thenReturn(Optional.of(user));
        when(rolePersistencePort.getRolById(user.getRoleId())).thenReturn(Optional.empty());

        assertThrows(RoleNotFoundException.class, () -> userUseCase.getUserByIdAuth(userId));
        verify(authPersistencePort).findById(userId);
    }

    @Test
    void saveUserEmployeeSuccessTest() {
        User user = TestDataFactory.mockUser();
        Role role = TestDataFactory.mockRole();
        role.setName(EMPLEADO);
        when(rolePersistencePort.getRolByName(EMPLEADO)).thenReturn(Optional.of(role));
        when(authPersistencePort.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        when(passwordEncodePort.encodePassword(user.getPassword())).thenReturn("encodedPassword");

        userUseCase.saveEmployee(user);

        verify(userPersistencePort, times(1)).saveUser(user);
        assertEquals("encodedPassword", user.getPassword());
    }

    @Test
    void saveUserEmployeeFailExistsTest() {
        User user = TestDataFactory.mockUser();
        Role role = TestDataFactory.mockRole();
        role.setName(EMPLEADO);
        when(rolePersistencePort.getRolByName(EMPLEADO)).thenReturn(Optional.of(role));
        when(authPersistencePort.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        assertThrows(UserAlreadyExistsException.class, () -> userUseCase.saveEmployee(user));
        verify(userPersistencePort, Mockito.never()).saveUser(any());
    }

    @Test
    void saveUserEmployeeFailInvalidEmailTest() {
        User user = TestDataFactory.mockUser();
        user.setEmail("correo-invalido");
        Role role = TestDataFactory.mockRole();
        role.setName(EMPLEADO);
        when(rolePersistencePort.getRolByName(EMPLEADO)).thenReturn(Optional.of(role));

        assertThrows(BadRequestException.class, () -> userUseCase.saveEmployee(user));
    }

}