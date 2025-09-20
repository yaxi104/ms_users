package com.hexagonal.ms_user.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthRequest {

    @NotBlank
    @Pattern(regexp = "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$", message = "Formato no válido")
    @Schema(description = "Correo electronico", example = "correo@gmail.com")
    private String email;

    @NotBlank
    @Schema(description = "Constraseña", example = "MiContraseñaSegurita")
    private String password;
}