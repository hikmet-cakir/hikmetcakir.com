package com.hikmetcakir.coreapi.service;

import com.hikmetcakir.coreapi.dto.user.UserQueryRequest;
import com.hikmetcakir.coreapi.dto.user.UserQueryResponse;
import com.hikmetcakir.coreapi.entity.UserEntity;
import com.hikmetcakir.coreapi.respository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.AssertionsKt.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    UserService userService;

    @Mock
    UserRepository userRepository;

    @Test
    void query_ShouldReturnUserResponse_WhenUserExists() {
        // Arrange
        UserQueryRequest request = new UserQueryRequest("test@example.com", "1234");

        UserEntity user = new UserEntity();
        user.setId("1");
        user.setEmail("test@example.com");
        user.setPassword("1234");
        user.setName("John");
        user.setSurname("Doe");

        when(userRepository.findByEmailAndPassword("test@example.com", "1234"))
                .thenReturn(Optional.of(user));

        // Act
        UserQueryResponse response = userService.query(request);

        // Assert
        assertNotNull(response);
        assertEquals("test@example.com", response.getEmail());
        assertEquals("John", response.getName());

        verify(userRepository, times(1))
                .findByEmailAndPassword("test@example.com", "1234");
    }

    @Test
    void query_ShouldReturnNull_WhenUserDoesNotExist() {
        // Arrange
        UserQueryRequest request = new UserQueryRequest("wrong@example.com", "wrong");

        when(userRepository.findByEmailAndPassword("wrong@example.com", "wrong"))
                .thenReturn(Optional.empty());

        // Act
        UserQueryResponse response = userService.query(request);

        // Assert
        assertNull(response);
        verify(userRepository, times(1))
                .findByEmailAndPassword("wrong@example.com", "wrong");
    }
}
