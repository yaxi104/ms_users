package com.hexagonal.ms_user.domain.spi;

import com.hexagonal.ms_user.domain.model.request.Role;

import java.util.Optional;

public interface IRolePersistencePort {

    Optional<Role> getRolById(Long id);

    void saveRole(Role role);

    Optional<Role> getRolByName(String name);
}
