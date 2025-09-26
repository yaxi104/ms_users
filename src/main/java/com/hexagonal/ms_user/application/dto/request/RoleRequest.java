package com.hexagonal.ms_user.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleRequest {

    @NotBlank
    @Schema(description = "Nombre del rol", example = "PROPIETARIO")
    private String name;

    @NotBlank
    @Schema(description = "Descripción del rol", example = "Propietario del restaurante")
    private String description;
}
