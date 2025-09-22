package com.hexagonal.ms_user.infrastructure.output.jpa.role.adapter;

import com.hexagonal.ms_user.domain.model.request.Role;
import com.hexagonal.ms_user.infrastructure.output.jpa.role.entity.RoleEntity;
import com.hexagonal.ms_user.infrastructure.output.jpa.role.mapper.IRoleEntityMapper;
import com.hexagonal.ms_user.infrastructure.output.jpa.role.repository.IRoleRepository;
import com.hexagonal.ms_user.util.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.hexagonal.ms_user.domain.utils.Constanst.PROPIETARIO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoleJpaAdapterTest {

    @InjectMocks
    private RoleJpaAdapter roleJpaAdapter;

    @Mock
    private IRoleRepository roleRepository;

    @Mock
    private IRoleEntityMapper roleEntityMapper;


    @Test
    void saveRoleSuccessTest() {
        RoleEntity mockEntity = new RoleEntity();
        Role mockRole = TestDataFactory.mockRole();
        when(roleEntityMapper.toEntity(mockRole)).thenReturn(mockEntity);
        roleJpaAdapter.saveRole(mockRole);
        verify(roleRepository).save(mockEntity);
    }

    @Test
    void getRolByIdSuccessTest() {
        Long id = 1L;
        RoleEntity mockEntity = new RoleEntity();
        Role mockRole = TestDataFactory.mockRole();

        when(roleRepository.findById(id)).thenReturn(Optional.of(mockEntity));
        when(roleEntityMapper.toRole(mockEntity)).thenReturn(mockRole);

        Optional<Role> result = roleJpaAdapter.getRolById(id);

        assertTrue(result.isPresent());
        assertEquals(mockRole, result.get());
        verify(roleRepository).findById(id);
        verify(roleEntityMapper).toRole(mockEntity);
    }

    @Test
    void getRolByNameTest() {
        String name = PROPIETARIO;
        RoleEntity mockEntity = new RoleEntity();
        Role mockRole = TestDataFactory.mockRole();

        when(roleRepository.findByName(name)).thenReturn(Optional.of(mockEntity));
        when(roleEntityMapper.toRole(mockEntity)).thenReturn(mockRole);

        Optional<Role> result = roleJpaAdapter.getRolByName(name);

        assertTrue(result.isPresent());
        assertEquals(mockRole, result.get());
        verify(roleRepository).findByName(name);
        verify(roleEntityMapper).toRole(mockEntity);
    }

}