package com.bank.api.service;

import com.bank.api.dto.AuthRequestDto;
import com.bank.api.dto.AuthResponseDto;
import com.bank.api.dto.RegisterRequestDto;

/**
 * Интерфейс для сервиса аутентификации.
 * <p>
 * Определяет методы регистрации и логина пользователя.
 */
public interface AuthServiceInterface {

    /**
     * Регистрирует нового пользователя.
     *
     * @param request DTO с данными для регистрации
     * @return DTO с JWT токеном
     */
    AuthResponseDto register(RegisterRequestDto request);

    /**
     * Логин пользователя и генерация токена.
     *
     * @param request DTO с логином и паролем
     * @return DTO с JWT токеном
     */
    AuthResponseDto login(AuthRequestDto request);
}
