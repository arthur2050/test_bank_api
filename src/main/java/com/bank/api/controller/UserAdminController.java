package com.bank.api.controller;

import com.bank.api.dto.UserCreateDto;
import com.bank.api.dto.UserDto;
import com.bank.api.service.UserServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class UserAdminController {
    // Управление пользователями

    @Autowired
    private UserServiceInterface userService;

    // Получить всех пользователей
    @GetMapping("/users")
    public List<UserDto> getAllUsers() {
        return userService.getAllUsers();
    }

    // Заблокировать пользователя (деактивировать)
    @PatchMapping("/user/{userId}/block")
    public void blockUser(@PathVariable Long userId) {
        userService.blockUser(userId);
    }

    // Активировать пользователя (включить)
    @PatchMapping("/user/{userId}/activate")
    public void activateUser(@PathVariable Long userId) {
        userService.activateUser(userId);
    }

    @PostMapping("/user")
    public void createUser(@RequestBody UserCreateDto userCreateDto) {
        userService.createUser(userCreateDto);
    }

    // Удалить пользователя
    @DeleteMapping("/user/{userId}")
    public void deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
    }
}
