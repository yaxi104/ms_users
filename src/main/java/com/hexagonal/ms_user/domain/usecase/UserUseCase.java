package com.hexagonal.ms_user.domain.usecase;

import com.hexagonal.ms_user.domain.api.IUserServicePort;
import com.hexagonal.ms_user.domain.exception.RoleNotFoundException;
import com.hexagonal.ms_user.domain.exception.UserAlreadyExistsException;
import com.hexagonal.ms_user.domain.exception.UserNotFoundException;
import com.hexagonal.ms_user.domain.model.request.Role;
import com.hexagonal.ms_user.domain.model.request.User;
import com.hexagonal.ms_user.domain.model.response.TokenResponse;
import com.hexagonal.ms_user.domain.model.response.UserAuth;
import com.hexagonal.ms_user.domain.spi.IAuthPersistencePort;
import com.hexagonal.ms_user.domain.spi.IAuthTokenResponsePort;
import com.hexagonal.ms_user.domain.spi.IPasswordEncodePort;
import com.hexagonal.ms_user.domain.spi.IRolePersistencePort;
import com.hexagonal.ms_user.domain.spi.IUserPersistencePort;
import com.hexagonal.ms_user.domain.utils.ValidateRequest;

import static com.hexagonal.ms_user.domain.utils.Constanst.PROPIETARIO;

public class UserUseCase implements IUserServicePort {

    private final IUserPersistencePort userPersistencePort;
    private final IAuthPersistencePort authPersistencePort;
    private final IPasswordEncodePort passwordEncodePort;
    private final IAuthTokenResponsePort authTokenResponsePort;
    private final IRolePersistencePort rolePersistencePort;

    public UserUseCase(IUserPersistencePort userPersistencePort, IAuthPersistencePort authPersistencePort, IPasswordEncodePort passwordEncodePort, IAuthTokenResponsePort authTokenResponsePort, IRolePersistencePort rolePersistencePort) {
        this.userPersistencePort = userPersistencePort;
        this.authPersistencePort = authPersistencePort;
        this.passwordEncodePort = passwordEncodePort;
        this.authTokenResponsePort = authTokenResponsePort;
        this.rolePersistencePort = rolePersistencePort;
    }

    @Override
    public TokenResponse authUser(User user) {
        validateCredentials(user);
        return authTokenResponsePort.getToken(user);
    }

    @Override
    public void saveUser(User user) {
        Role role = rolePersistencePort.getRolByName(PROPIETARIO).orElseThrow(RoleNotFoundException::new);
        user.setRoleId(role.getId());
        validateUserRequest(user);
        if (authPersistencePort.findByEmail(user.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException();
        }
        userPersistencePort.saveUser(user);
    }

    @Override
    public User getUserByEmail(String email) {
        return authPersistencePort.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);
    }

    @Override
    public UserAuth getUserByIdAuth(Long id) {
        return authPersistencePort.findById(id)
                .map(this::getUserAuth)
                .orElseThrow(UserNotFoundException::new);
    }

    @Override
    public User getUserById(Long id) {
        return authPersistencePort.findById(id)
                .orElseThrow(UserNotFoundException::new);
    }

    private void validateUserRequest(User user) {
        ValidateRequest.checkNotBlank(user.getFirstName());
        ValidateRequest.checkNotBlank(user.getLastName());
        ValidateRequest.checkIdNumber(user.getIdNumber());
        ValidateRequest.checkNumberPhone(user.getPhoneNumber());
        ValidateRequest.checkPastDate(user.getDateBirth());
        validateCredentials(user);
        user.setPassword(passwordEncodePort.encodePassword(user.getPassword()));
    }

    private void validateCredentials(User user) {
        ValidateRequest.checkEmail(user.getEmail());
        ValidateRequest.checkNotBlank(user.getPassword());
    }

    private UserAuth getUserAuth(User user) {
        Role role = rolePersistencePort.getRolById(user.getRoleId()).orElseThrow(RoleNotFoundException::new);
        UserAuth userAuth = new UserAuth();
        userAuth.setId(user.getId());
        userAuth.setRole(role.getName());
        userAuth.setEmail(user.getEmail());
        return userAuth;
    }

}
