package com.hexagonal.ms_user.application.handler;

import com.hexagonal.ms_user.application.dto.request.RoleRequest;
import com.hexagonal.ms_user.application.dto.response.RoleResponse;

public interface IRoleHandler {

    void saveRole(RoleRequest roleRequest);

    RoleResponse getRolById(Long id);

    RoleResponse getRolByName(String name);
}
