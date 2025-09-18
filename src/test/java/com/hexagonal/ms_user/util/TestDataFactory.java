package com.hexagonal.ms_user.util;

import com.hexagonal.ms_user.application.dto.request.UserRequest;
import com.hexagonal.ms_user.domain.model.User;
import com.hexagonal.ms_user.infrastructure.output.jpa.entity.UserEntity;

import java.time.LocalDate;

public class TestDataFactory {

    private TestDataFactory() {
    }

    public static UserRequest mockUserRequest() {
        UserRequest userRequest = new UserRequest();
        userRequest.setFirstName("Pepito");
        userRequest.setLastName("Perez");
        userRequest.setIdNumber("1234");
        userRequest.setPhoneNumber("+573167549634");
        userRequest.setDateBirth(LocalDate.of(2000, 9, 17));
        userRequest.setEmail("test@example.com");
        userRequest.setPassword("password123");
        return userRequest;
    }

    public static User mockUser() {
        User user = new User();
        user.setFirstName("Pepito");
        user.setLastName("Perez");
        user.setIdNumber("1234");
        user.setPhoneNumber("+573167549634");
        user.setDateBirth(LocalDate.of(2000, 9, 17));
        user.setEmail("test@example.com");
        user.setPassword("password123");
        user.setRole("PROPIETARIO");
        return user;
    }

    public static UserEntity mockUserEntity() {
        var mockUserEntity = new UserEntity();
        mockUserEntity.setFirstName("Pepito");
        mockUserEntity.setLastName("Perez");
        mockUserEntity.setIdNumber("1234");
        mockUserEntity.setPhoneNumber("+573167549634");
        mockUserEntity.setDateBirth(LocalDate.of(2000, 9, 17));
        mockUserEntity.setEmail("test@example.com");
        mockUserEntity.setPassword("password123");
        mockUserEntity.setRole("PROPIETARIO");
        return mockUserEntity;
    }
}
