package com.bank.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Конфигурационный класс для настройки аутентификации и шифрования паролей.
 * <p>
 * Предоставляет бины для PasswordEncoder и AuthenticationManager,
 * используемые в сервисах аутентификации Spring Security.
 */
@Configuration
public class AuthConfig {


    /**
     * Создает бин для шифрования паролей с использованием BCrypt.
     * <p>
     * Используется в сервисах регистрации и аутентификации пользователей.
     *
     * @return PasswordEncoder экземпляр для хэширования паролей
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Создает бин AuthenticationManager.
     * <p>
     * Необходим для выполнения аутентификации пользователя
     * через AuthenticationManager.authenticate().
     *
     * @param configuration конфигурация аутентификации Spring
     * @return AuthenticationManager экземпляр для аутентификации
     * @throws Exception если не удалось создать AuthenticationManager
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}
