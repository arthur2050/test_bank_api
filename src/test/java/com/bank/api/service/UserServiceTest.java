package com.bank.api.service;

import com.bank.api.dto.UserCreateDto;
import com.bank.api.entity.Role;
import com.bank.api.entity.User;
import com.bank.api.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        userService = new UserService(userRepository, passwordEncoder);
    }

    @Test
    void createUser_success() {
        UserCreateDto dto = new UserCreateDto();
        dto.setUsername("john");
        dto.setPassword("secret");
        dto.setRole(Role.USER);

        when(userRepository.existsByUsername("john")).thenReturn(false);
        when(passwordEncoder.encode("secret")).thenReturn("hashedSecret");

        userService.createUser(dto);

        verify(userRepository).save(argThat(user ->
                user.getUsername().equals("john") &&
                        user.getPassword().equals("hashedSecret") &&
                        user.getRole() == Role.USER
        ));
    }

    @Test
    void createUser_usernameExists_throws() {
        when(userRepository.existsByUsername("john")).thenReturn(true);

        UserCreateDto dto = new UserCreateDto();
        dto.setUsername("john");
        dto.setPassword("secret");
        dto.setRole(Role.USER);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> userService.createUser(dto));
        assertEquals("Пользователь с таким именем уже существует", ex.getMessage());
    }

    @Test
    void createUser_invalidRole_throws() {
        UserCreateDto dto = new UserCreateDto();
        dto.setUsername("john");
        dto.setPassword("secret");
        dto.setRole(null);

        when(userRepository.existsByUsername("john")).thenReturn(false);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> userService.createUser(dto));
        assertTrue(ex.getMessage().contains("Недопустимая роль"));
    }

    @Test
    void blockUser_success() {
        User user = createFakeUser();
        user.setEnabled(true);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        userService.blockUser(1L);

        assertFalse(user.isEnabled());
        verify(userRepository).save(user);
    }

    @Test
    void activateUser_success() {
        User user = createFakeUser();
        user.setUsername("john");
        user.setPassword("hashed");
        user.setRole(Role.USER);
        user.setEnabled(false);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        userService.activateUser(1L);

        assertTrue(user.isEnabled());
        verify(userRepository).save(user);
    }

    @Test
    void getAllUsers_returnsDtoList() {
        User user = createFakeUser();
        user.setEnabled(true);

        when(userRepository.findAll()).thenReturn(List.of(user));

        List<?> users = userService.getAllUsers();
        assertEquals(1, users.size());
    }

    @Test
    void deleteUser_callsRepository() {
        doNothing().when(userRepository).deleteById(1L);

        userService.deleteUser(1L);

        verify(userRepository).deleteById(1L);
    }

    private User createFakeUser() {
        User user = new User();
        user.setUsername("john");
        user.setPassword("secret");
        user.setRole(Role.USER);

        return user;
    }
}
