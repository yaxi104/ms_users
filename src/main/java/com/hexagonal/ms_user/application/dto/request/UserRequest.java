package com.hexagonal.ms_user.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UserRequest {

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    @Pattern(regexp = "\\d+", message = "Formato no válido")
    private String idNumber;

    @NotBlank
    @Size(max = 13)
    @Pattern(regexp = "^\\+?\\d{7,13}$", message = "Formato no válido")
    private String phoneNumber;

    @NotNull
    @Past
    private LocalDate dateBirth;

    @NotBlank
    @Pattern(regexp = "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$", message = "Formato no válido")
    private String email;

    @NotBlank
    private String password;
}
