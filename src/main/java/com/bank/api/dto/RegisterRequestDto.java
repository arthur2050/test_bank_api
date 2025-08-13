package com.bank.api.dto;

import com.bank.api.entity.Role;
import lombok.Data;

/**
 * DTO для регистрации пользователя.
 * <p>
 * Содержит логин, пароль и роль пользователя.
 */
@Data
public class RegisterRequestDto {
    private String username;
    private String password;
    private Role role;
}