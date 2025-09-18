package com.hexagonal.ms_user.infrastructure.output.jpa.adapter;

import com.hexagonal.ms_user.domain.model.User;
import com.hexagonal.ms_user.infrastructure.exception.UserAlreadyExistsException;
import com.hexagonal.ms_user.infrastructure.output.jpa.mapper.IUserEntityMapper;
import com.hexagonal.ms_user.infrastructure.output.jpa.repository.IUserRepository;
import com.hexagonal.ms_user.util.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserJpaAdapterTest {

    @InjectMocks
    private UserJpaAdapter userJpaAdapter;

    @Mock
    private IUserRepository userRepository;

    @Mock
    private IUserEntityMapper userEntityMapper;


    @Test
    void saveUserSuccessTest() {
        var mockEntity = TestDataFactory.mockUserEntity();
        var mockUser = TestDataFactory.mockUser();

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());
        when(userEntityMapper.toEntity(mockUser)).thenReturn(mockEntity);

        userJpaAdapter.saveUser(mockUser);

        verify(userRepository).save(mockEntity);

    }

    @Test
    void saveUserExistsTest() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(TestDataFactory.mockUserEntity()));
        User user = TestDataFactory.mockUser();
        assertThrows(UserAlreadyExistsException.class, () -> userJpaAdapter.saveUser(user));
    }

}

