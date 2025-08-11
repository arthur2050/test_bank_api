package com.bank.api.service;

import com.bank.api.dto.UserCreateDto;
import com.bank.api.dto.UserDto;

import java.util.List;

public interface UserServiceInterface {
    List<UserDto> getAllUsers();

    void blockUser(Long userId);

    void activateUser(Long userId);

    void createUser(UserCreateDto userCreateDto);

    void deleteUser(Long userId);
}
