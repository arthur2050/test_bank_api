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

    @Autowired
    private UserServiceInterface userService;

    @GetMapping("/users")
    public List<UserDto> getAllUsers() {
        return userService.getAllUsers();
    }

    @PatchMapping("/user/{userId}/block")
    public void blockUser(@PathVariable Long userId) {
        userService.blockUser(userId);
    }

    @PatchMapping("/user/{userId}/activate")
    public void activateUser(@PathVariable Long userId) {
        userService.activateUser(userId);
    }

    @PostMapping("/user")
    public void createUser(@RequestBody UserCreateDto userCreateDto) {
        userService.createUser(userCreateDto);
    }

    @DeleteMapping("/user/{userId}")
    public void deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
    }
}
