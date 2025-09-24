package com.hexagonal.ms_user.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UserResponse {

    @Schema(description = "Id del usuario", example = "1")
    private Long id;

    @Schema(description = "Nombre del propietario", example = "Carlos")
    private String firstName;

    @Schema(description = "Apellido del propietario", example = "Pérez")
    private String lastName;

    @Schema(description = "Documento de identidad (solo números)", example = "1234567890")
    private String idNumber;

    @Schema(description = "Número de celular. Máximo 13 caracteres. Puede comenzar con +", example = "+573001234567")
    private String phoneNumber;

    @Schema(description = "Fecha de nacimiento. Debe ser una fecha en el pasado", example = "1990-05-15")
    private LocalDate dateBirth;

    @Schema(description = "Correo electrónico válido", example = "carlos.perez@example.com")
    private String email;

    @Schema(description = "Contraseña del usuario. Se debe enviar en texto plano, será encriptada internamente", example = "MiClaveSegura123")
    private String password;

    @Schema(description = "Role del usuario", example = "1")
    private Long roleId;

    @Schema(description = "Id del restaurante solo para propietario", example = "1")
    private Long restaurantId;
}
