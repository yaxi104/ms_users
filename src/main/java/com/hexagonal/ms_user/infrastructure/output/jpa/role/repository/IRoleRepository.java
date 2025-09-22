package com.hexagonal.ms_user.infrastructure.output.jpa.role.repository;

import com.hexagonal.ms_user.infrastructure.output.jpa.role.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IRoleRepository extends JpaRepository<RoleEntity, Long> {

    Optional<RoleEntity> findByName(String name);
}
