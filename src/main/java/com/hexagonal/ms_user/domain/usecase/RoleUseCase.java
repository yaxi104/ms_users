package com.hexagonal.ms_user.domain.usecase;

import com.hexagonal.ms_user.domain.api.IRoleServicePort;
import com.hexagonal.ms_user.domain.exception.RoleAlreadyExistsException;
import com.hexagonal.ms_user.domain.exception.RoleNotFoundException;
import com.hexagonal.ms_user.domain.model.request.Role;
import com.hexagonal.ms_user.domain.spi.IRolePersistencePort;
import com.hexagonal.ms_user.domain.utils.ValidateRequest;

import static com.hexagonal.ms_user.domain.utils.Constanst.PATTERN_ONLY_UPPERCASE;

public class RoleUseCase implements IRoleServicePort {

    private final IRolePersistencePort rolePersistencePort;

    public RoleUseCase(IRolePersistencePort rolePersistencePort) {
        this.rolePersistencePort = rolePersistencePort;
    }

    @Override
    public Role getRolById(Long id) {
        return rolePersistencePort.getRolById(id).orElseThrow(RoleNotFoundException::new);
    }

    @Override
    public void saveRole(Role role) {
        ValidateRequest.checkNotBlank(role.getName());
        ValidateRequest.checkPattern(role.getName(), PATTERN_ONLY_UPPERCASE);
        ValidateRequest.checkNotBlank(role.getDescription());
        if (rolePersistencePort.getRolByName(role.getName()).isPresent()) {
            throw new RoleAlreadyExistsException();
        }
        rolePersistencePort.saveRole(role);
    }

    @Override
    public Role getRolByName(String name) {
        return rolePersistencePort.getRolByName(name).orElseThrow(RoleNotFoundException::new);
    }
}
