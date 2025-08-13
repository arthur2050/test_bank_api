package com.bank.api.service;

import com.bank.api.dto.UserCreateDto;
import com.bank.api.dto.UserDto;

import java.util.List;

/**
 * Интерфейс для сервиса управления пользователями.
 */
public interface UserServiceInterface {
    /**
     * Возвращает список всех пользователей.
     */
    List<UserDto> getAllUsers();

    /**
     * Блокирует пользователя по ID.
     */
    void blockUser(Long userId);

    /**
     * Активирует пользователя по ID.
     */
    void activateUser(Long userId);

    /**
     * Создает нового пользователя.
     *
     * @param userCreateDto DTO с данными нового пользователя
     */
    void createUser(UserCreateDto userCreateDto);

    /**
     * Удаляет пользователя по ID.
     */
    void deleteUser(Long userId);
}
