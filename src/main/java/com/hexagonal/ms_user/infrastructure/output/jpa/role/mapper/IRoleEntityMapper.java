package com.hexagonal.ms_user.infrastructure.output.jpa.role.mapper;

import com.hexagonal.ms_user.domain.model.request.Role;
import com.hexagonal.ms_user.infrastructure.output.jpa.role.entity.RoleEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IRoleEntityMapper {

    RoleEntity toEntity(Role user);

    Role toRole(RoleEntity roleEntity);

}
