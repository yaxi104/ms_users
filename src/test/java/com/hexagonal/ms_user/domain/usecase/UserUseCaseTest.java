package com.hexagonal.ms_user.domain.usecase;

import com.hexagonal.ms_user.domain.spi.IUserPersistencePort;
import com.hexagonal.ms_user.util.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserUseCaseTest {

    @InjectMocks
    private UserUseCase userUseCase;

    @Mock
    private IUserPersistencePort userPersistencePort;

    @Test
    void saveUserSuccessTest() {
        var mockUser = TestDataFactory.mockUser();

        userUseCase.saveUser(mockUser);

        verify(userPersistencePort, times(1)).saveUser(mockUser);

    }
}