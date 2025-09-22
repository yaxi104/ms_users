package com.hexagonal.ms_user.infrastructure.output.jpa.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "USUARIOS")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false, length = 100)
    private String firstName;

    @Column(name = "apellido", nullable = false, length = 100)
    private String lastName;

    @Column(name = "numero_documento", nullable = false, length = 20)
    private String idNumber;

    @Column(name = "celular", nullable = false, length = 13)
    private String phoneNumber;

    @Column(name = "fecha_nacimiento", nullable = false)
    private LocalDate dateBirth;

    @Column(name = "correo", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "clave", nullable = false)
    private String password;

    @Column(name = "id_rol")
    private Long roleId;

}
