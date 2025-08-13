package com.bank.api.controller;

import com.bank.api.dto.AuthRequestDto;
import com.bank.api.dto.RegisterRequestDto;
import com.bank.api.service.AuthServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Контроллер для работы с аутентификацией и регистрацией пользователей.
 * <p>
 * Предоставляет эндпоинты для регистрации и входа в систему.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthServiceInterface authService;

    /**
     * Регистрирует нового пользователя.
     *
     * @param request DTO с данными регистрации
     * @return токен авторизации или информация об ошибке
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequestDto request) {
        return ResponseEntity.ok(authService.register(request));
    }

    /**
     * Авторизация пользователя (логин).
     *
     * @param request DTO с логином и паролем
     * @return токен авторизации
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequestDto request) {
        return ResponseEntity.ok(authService.login(request));
    }
}
