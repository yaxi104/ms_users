package com.hexagonal.ms_user.infrastructure.output.jpa.role.adapter;

import com.hexagonal.ms_user.domain.model.request.Role;
import com.hexagonal.ms_user.domain.spi.IRolePersistencePort;
import com.hexagonal.ms_user.infrastructure.output.jpa.role.mapper.IRoleEntityMapper;
import com.hexagonal.ms_user.infrastructure.output.jpa.role.repository.IRoleRepository;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class RoleJpaAdapter implements IRolePersistencePort {

    private final IRoleRepository roleRepository;

    private final IRoleEntityMapper roleEntityMapper;


    @Override
    public Optional<Role> getRolById(Long id) {
        return roleRepository.findById(id).map(roleEntityMapper::toRole);
    }

    @Override
    public void saveRole(Role role) {
        roleRepository.save(roleEntityMapper.toEntity(role));
    }

    @Override
    public Optional<Role> getRolByName(String name) {
        return roleRepository.findByName(name).map(roleEntityMapper::toRole);
    }
}
