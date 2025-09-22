package com.hexagonal.ms_user.application.handler.impl;

import com.hexagonal.ms_user.application.dto.request.RoleRequest;
import com.hexagonal.ms_user.application.dto.response.RoleResponse;
import com.hexagonal.ms_user.application.handler.IRoleHandler;
import com.hexagonal.ms_user.application.mapper.IRoleMapper;
import com.hexagonal.ms_user.domain.api.IRoleServicePort;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class RoleHandler implements IRoleHandler {

    private final IRoleServicePort roleServicePort;
    private final IRoleMapper roleMapper;

    @Override
    public void saveRole(RoleRequest roleRequest) {
        roleServicePort.saveRole(roleMapper.toRole(roleRequest));
    }

    @Override
    public RoleResponse getRolById(Long id) {
        return roleMapper.toRoleResponse(roleServicePort.getRolById(id));
    }

    @Override
    public RoleResponse getRolByName(String name) {
        return roleMapper.toRoleResponse(roleServicePort.getRolByName(name));
    }
}
