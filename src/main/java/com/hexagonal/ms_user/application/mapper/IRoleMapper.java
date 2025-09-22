package com.hexagonal.ms_user.application.mapper;

import com.hexagonal.ms_user.application.dto.request.RoleRequest;
import com.hexagonal.ms_user.application.dto.response.RoleResponse;
import com.hexagonal.ms_user.domain.model.request.Role;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IRoleMapper {

    Role toRole(RoleRequest roleRequest);

    RoleResponse toRoleResponse(Role role);

}
