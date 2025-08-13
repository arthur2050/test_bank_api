package com.bank.api.dto;

import lombok.Data;


/**
 * DTO для запроса авторизации.
 * <p>
 * Содержит логин и пароль пользователя.
 */
@Data
public class AuthRequestDto {
    private String username;
    private String password;
}