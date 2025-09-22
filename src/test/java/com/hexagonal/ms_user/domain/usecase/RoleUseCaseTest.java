package com.hexagonal.ms_user.domain.usecase;

import com.hexagonal.ms_user.domain.exception.RoleAlreadyExistsException;
import com.hexagonal.ms_user.domain.exception.RoleNotFoundException;
import com.hexagonal.ms_user.domain.model.request.Role;
import com.hexagonal.ms_user.domain.spi.IRolePersistencePort;
import com.hexagonal.ms_user.util.TestDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

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
class RoleUseCaseTest {

    private IRolePersistencePort rolePersistencePort;

    private RoleUseCase roleUseCase;

    @BeforeEach
    void setUp() {
        rolePersistencePort = mock(IRolePersistencePort.class);
        roleUseCase = new RoleUseCase(rolePersistencePort);
    }

    @Test
    void saveRoleSuccessTest() {
        Role role = TestDataFactory.mockRole();
        when(rolePersistencePort.getRolByName(PROPIETARIO)).thenReturn(Optional.empty());
        roleUseCase.saveRole(role);

        verify(rolePersistencePort, times(1)).saveRole(role);
    }

    @Test
    void saveRoleFailExistsTest() {
        Role role = TestDataFactory.mockRole();
        when(rolePersistencePort.getRolByName(PROPIETARIO)).thenReturn(Optional.of(TestDataFactory.mockRole()));

        assertThrows(RoleAlreadyExistsException.class, () -> roleUseCase.saveRole(role));
        verify(rolePersistencePort, Mockito.never()).saveRole(any());
    }


    @Test
    void getRolByIdSuccessTest() {
        Long roleId = 1L;
        Role mockRole = TestDataFactory.mockRole();

        when(rolePersistencePort.getRolById(roleId)).thenReturn(Optional.of(mockRole));

        Role result = roleUseCase.getRolById(roleId);

        assertNotNull(result);
        assertEquals(roleId, result.getId());
        verify(rolePersistencePort).getRolById(roleId);
    }

    @Test
    void getRoleByIdWhenUserDoesNotExist() {
        Long roleId = 99L;
        when(rolePersistencePort.getRolById(roleId)).thenReturn(Optional.empty());

        assertThrows(RoleNotFoundException.class, () -> roleUseCase.getRolById(roleId));
        verify(rolePersistencePort).getRolById(roleId);
    }

    @Test
    void getRoleByNameSuccesTest() {
        String name = PROPIETARIO;
        Role mockRole = TestDataFactory.mockRole();

        when(rolePersistencePort.getRolByName(name)).thenReturn(Optional.of(mockRole));

        Role result = roleUseCase.getRolByName(name);

        assertNotNull(result);
        assertEquals(name, result.getName());
        verify(rolePersistencePort).getRolByName(name);
    }

    @Test
    void getRoleByNameNotExistTest() {
        String name = "USER";
        when(rolePersistencePort.getRolByName(name)).thenReturn(Optional.empty());

        assertThrows(RoleNotFoundException.class, () -> roleUseCase.getRolByName(name));
        verify(rolePersistencePort).getRolByName(name);
    }

}