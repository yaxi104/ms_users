package com.hexagonal.ms_user.domain.spi;

public interface IPasswordEncodePort {

    String encodePassword(String password);

}
