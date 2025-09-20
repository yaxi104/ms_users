package com.hexagonal.ms_user.application.mapper;

import com.hexagonal.ms_user.application.dto.response.UserResponse;
import com.hexagonal.ms_user.domain.model.request.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IUserMapper {

    UserResponse toResponse(User user);

}
