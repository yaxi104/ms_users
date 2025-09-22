package com.hexagonal.ms_user.application.handler.impl;

import com.hexagonal.ms_user.application.dto.request.RoleRequest;
import com.hexagonal.ms_user.application.dto.response.RoleResponse;
import com.hexagonal.ms_user.application.mapper.IRoleMapper;
import com.hexagonal.ms_user.domain.api.IRoleServicePort;
import com.hexagonal.ms_user.domain.model.request.Role;
import com.hexagonal.ms_user.util.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.hexagonal.ms_user.domain.utils.Constanst.ROLE_PROPIETARIO;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoleHandlerTest {
    @InjectMocks
    private RoleHandler roleHandler;

    @Mock
    private IRoleServicePort roleServicePort;

    @Mock
    private IRoleMapper roleMapper;

    @Test
    void saveRoleSuccessTest() {
        RoleRequest mockRoleRequest = new RoleRequest();
        roleHandler.saveRole(mockRoleRequest);

        verify(roleMapper).toRole(mockRoleRequest);

        verify(roleMapper).toRole(mockRoleRequest);
    }

    @Test
    void getRolByIdSuccessTest() {
        Long roleId = 1L;
        Role role = TestDataFactory.mockRole();

        when(roleServicePort.getRolById(roleId)).thenReturn(role);
        when(roleMapper.toRoleResponse(role)).thenReturn(new RoleResponse());

        RoleResponse result = roleHandler.getRolById(roleId);

        assertNotNull(result);
        verify(roleServicePort).getRolById(roleId);
        verify(roleMapper).toRoleResponse(role);
    }

    @Test
    void getRolByNameSuccessTest() {
        String name = ROLE_PROPIETARIO;
        Role role = TestDataFactory.mockRole();

        when(roleServicePort.getRolByName(name)).thenReturn(role);
        when(roleMapper.toRoleResponse(role)).thenReturn(new RoleResponse());

        RoleResponse result = roleHandler.getRolByName(name);

        assertNotNull(result);
        verify(roleServicePort).getRolByName(name);
        verify(roleMapper).toRoleResponse(role);
    }

}