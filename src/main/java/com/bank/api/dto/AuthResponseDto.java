package com.bank.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * DTO для ответа авторизации.
 * <p>
 * Содержит JWT токен пользователя.
 */
@Data
@AllArgsConstructor
public class AuthResponseDto {
    private String token;
}