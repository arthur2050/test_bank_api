package com.bank.api.controller;

import com.bank.api.dto.UserCreateDto;
import com.bank.api.dto.UserDto;
import com.bank.api.service.UserServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Контроллер для администраторов для управления пользователями.
 * <p>
 * Позволяет администратору:
 * <ul>
 *     <li>Просматривать список всех пользователей</li>
 *     <li>Блокировать и активировать пользователей</li>
 *     <li>Создавать новых пользователей</li>
 *     <li>Удалять пользователей</li>
 * </ul>
 * Базовый URL: /api/admin
 */
@RestController
@RequestMapping("/api/admin")
public class UserAdminController {

    @Autowired
    private UserServiceInterface userService;

    /**
     * Возвращает список всех пользователей.
     *
     * @return список DTO пользователей
     */
    @GetMapping("/users")
    public List<UserDto> getAllUsers() {
        return userService.getAllUsers();
    }

    /**
     * Блокирует пользователя по ID.
     *
     * @param userId ID пользователя для блокировки
     */
    @PatchMapping("/user/{userId}/block")
    public void blockUser(@PathVariable Long userId) {
        userService.blockUser(userId);
    }

    /**
     * Активирует пользователя по ID.
     *
     * @param userId ID пользователя для активации
     */
    @PatchMapping("/user/{userId}/activate")
    public void activateUser(@PathVariable Long userId) {
        userService.activateUser(userId);
    }

    /**
     * Создает нового пользователя.
     *
     * @param userCreateDto DTO с данными нового пользователя (username, password, role)
     */
    @PostMapping("/user")
    public void createUser(@RequestBody UserCreateDto userCreateDto) {
        userService.createUser(userCreateDto);
    }


    /**
     * Удаляет пользователя по ID.
     *
     * @param userId ID пользователя для удаления
     */
    @DeleteMapping("/user/{userId}")
    public void deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
    }
}
