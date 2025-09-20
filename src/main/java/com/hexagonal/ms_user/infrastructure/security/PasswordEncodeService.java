package com.hexagonal.ms_user.infrastructure.security;

import com.hexagonal.ms_user.domain.spi.IPasswordEncodePort;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PasswordEncodeService implements IPasswordEncodePort {

    private final PasswordEncoder passwordEncoder;

    @Override
    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

}
