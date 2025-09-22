package com.hexagonal.ms_user.infrastructure.output.jpa.user.repository;

import com.hexagonal.ms_user.infrastructure.output.jpa.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IUserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByEmail(String email);
}
