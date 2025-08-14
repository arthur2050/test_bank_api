package com.bank.api.service;

import com.bank.api.dto.UserCreateDto;
import com.bank.api.dto.UserDto;
import com.bank.api.entity.Role;
import com.bank.api.entity.User;
import com.bank.api.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Сервис для работы с пользователями.
 * <p>
 * Содержит бизнес-логику создания, блокировки, активации и удаления пользователей.
 */
@Service
public class UserService implements UserServiceInterface {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Возвращает список всех пользователей.
     *
     * @return список DTO пользователей
     */
    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserDto::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Блокирует пользователя (устанавливает enabled = false).
     *
     * @param userId ID пользователя
     */
    @Override
    @Transactional
    public void blockUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setEnabled(false);
        userRepository.save(user);
    }

    /**
     * Активирует пользователя (устанавливает enabled = true).
     *
     * @param userId ID пользователя
     */
    @Override
    @Transactional
    public void activateUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setEnabled(true);
        userRepository.save(user);
    }

    /**
     * Создаёт нового пользователя.
     *
     * @param dto DTO с данными нового пользователя
     */
    @Override
    public void createUser(UserCreateDto dto) {
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new RuntimeException("Пользователь с таким именем уже существует");
        }

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        if (!EnumSet.allOf(Role.class).contains(dto.getRole())) {
            throw new IllegalArgumentException("Недопустимая роль: " + dto.getRole());
        }

        user.setRole(dto.getRole());

        userRepository.save(user);
    }

    /**
     * Удаляет пользователя по ID.
     *
     * @param userId ID пользователя
     */
    @Override
    @Transactional
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }
}
