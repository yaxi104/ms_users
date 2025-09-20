package com.hexagonal.ms_user.domain.spi;

import com.hexagonal.ms_user.domain.model.request.User;
import com.hexagonal.ms_user.domain.model.response.TokenResponse;

public interface IAuthTokenResponsePort {

    TokenResponse getToken(User user);
}
