package com.hexagonal.ms_user.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserEmployeeRequest {

    @NotBlank
    @Schema(description = "Nombre del empleado", example = "Carlos")
    private String firstName;

    @NotBlank
    @Schema(description = "Apellido del empleado", example = "Pérez")
    private String lastName;

    @NotBlank
    @Pattern(regexp = "\\d+", message = "Formato no válido")
    @Schema(description = "Documento de identidad (solo números)", example = "1234567890")
    private String idNumber;

    @NotBlank
    @Size(max = 13)
    @Pattern(regexp = "^\\+?\\d{7,13}$", message = "Formato no válido")
    @Schema(description = "Número de celular. Máximo 13 caracteres. Puede comenzar con +", example = "+573001234567")
    private String phoneNumber;

    @NotBlank
    @Pattern(regexp = "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$", message = "Formato no válido")
    @Schema(description = "Correo electrónico válido", example = "carlos.perez@example.com")
    private String email;

    @NotBlank
    @Schema(description = "Contraseña del usuario. Se debe enviar en texto plano, será encriptada internamente", example = "MiClaveSegura123")
    private String password;

    @NotBlank
    @Schema(description = "Id del restaurante", example = "1")
    private Long restaurantId;
}
