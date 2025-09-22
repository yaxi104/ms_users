package com.hexagonal.ms_user.infrastructure.output.jpa.user.mapper;

import com.hexagonal.ms_user.domain.model.request.User;
import com.hexagonal.ms_user.infrastructure.output.jpa.user.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IUserEntityMapper {

    UserEntity toEntity(User user);

    User toUser(UserEntity user);

}
