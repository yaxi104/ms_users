package com.hexagonal.ms_user.application.mapper;

import com.hexagonal.ms_user.application.dto.request.UserRequest;
import com.hexagonal.ms_user.domain.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IUserRequestMapper {

    User toUser(UserRequest userRequest);

}
