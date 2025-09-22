package com.hexagonal.ms_user.infrastructure.output.jpa.adapter;

import com.hexagonal.ms_user.domain.model.request.User;
import com.hexagonal.ms_user.infrastructure.output.jpa.user.entity.UserEntity;
import com.hexagonal.ms_user.infrastructure.output.jpa.user.mapper.IUserEntityMapper;
import com.hexagonal.ms_user.infrastructure.output.jpa.user.repository.IUserRepository;
import com.hexagonal.ms_user.infrastructure.output.jpa.user.adapter.AuthJpaAdapter;
import com.hexagonal.ms_user.util.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthJpaAdapterTest {
    @InjectMocks
    private AuthJpaAdapter authJpaAdapter;

    @Mock
    private IUserRepository userRepository;

    @Mock
    private IUserEntityMapper userEntityMapper;

    @Test
    void findByEmailWhenUserExistsTest() {
        String email = "test@example.com";
        UserEntity mockEntity = TestDataFactory.mockUserEntity();
        User expectedUser = TestDataFactory.mockUser();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(mockEntity));
        when(userEntityMapper.toUser(mockEntity)).thenReturn(expectedUser);

        Optional<User> result = authJpaAdapter.findByEmail(email);

        assertTrue(result.isPresent());
        assertEquals(expectedUser, result.get());
        verify(userRepository).findByEmail(email);
        verify(userEntityMapper).toUser(mockEntity);
    }

    @Test
    void findByEmailWhenUserDoesNotExistTest() {
        String email = "notfound@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        Optional<User> result = authJpaAdapter.findByEmail(email);

        assertTrue(result.isEmpty());
        verify(userRepository).findByEmail(email);
        verify(userEntityMapper, never()).toUser(any());
    }

    @Test
    void findByIdWhenUserExistsTest() {
        Long id = 1L;
        UserEntity mockEntity = TestDataFactory.mockUserEntity();
        User expectedUser = TestDataFactory.mockUser();

        when(userRepository.findById(id)).thenReturn(Optional.of(mockEntity));
        when(userEntityMapper.toUser(mockEntity)).thenReturn(expectedUser);

        Optional<User> result = authJpaAdapter.findById(id);

        assertTrue(result.isPresent());
        assertEquals(expectedUser, result.get());
        verify(userRepository).findById(id);
        verify(userEntityMapper).toUser(mockEntity);
    }

    @Test
    void findByIdWhenUserDoesNotExistTest() {
        Long id = 2L;
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        Optional<User> result = authJpaAdapter.findById(id);

        assertTrue(result.isEmpty());
        verify(userRepository).findById(id);
        verify(userEntityMapper, never()).toUser(any());
    }
}