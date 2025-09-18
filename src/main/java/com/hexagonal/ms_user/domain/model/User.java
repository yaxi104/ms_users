package com.hexagonal.ms_user.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private Long id;
    private String firstName;
    private String lastName;
    private String idNumber;
    private String phoneNumber;
    private LocalDate dateBirth;
    private String email;
    private String password;
    private String role;
}
