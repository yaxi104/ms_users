package com.hexagonal.ms_user.application.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleResponse {

    private Long id;

    private String name;

    private String description;

}