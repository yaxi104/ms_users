package com.hexagonal.ms_user.domain.api;

import com.hexagonal.ms_user.domain.model.request.Role;

public interface IRoleServicePort {

    Role getRolById(Long id);

    void saveRole(Role role);

    Role getRolByName(String name);
}
