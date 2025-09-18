package com.hexagonal.ms_user.application.handler.impl;

import com.hexagonal.ms_user.application.dto.request.UserRequest;
import com.hexagonal.ms_user.application.handler.IUserHandler;
import com.hexagonal.ms_user.application.mapper.IUserRequestMapper;
import com.hexagonal.ms_user.domain.api.IUserServicePort;
import com.hexagonal.ms_user.infrastructure.exception.UserNotOlderAgeException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;

@Service
@RequiredArgsConstructor
@Transactional
public class UserHandler implements IUserHandler {

    private final IUserServicePort userServicePort;
    private final IUserRequestMapper userRequestMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void saveUser(UserRequest userRequest) {
        validateOlderAge(userRequest.getDateBirth());
        var user = userRequestMapper.toUser(userRequest);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("PROPIETARIO");
        userServicePort.saveUser(user);
    }

    private void validateOlderAge(LocalDate birthDate) {
        if (Period.between(birthDate, LocalDate.now()).getYears() < 18) {
            throw new UserNotOlderAgeException();
        }
    }
}
