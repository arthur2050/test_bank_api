package com.bank.api.exception;


import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Глобальный обработчик исключений для REST-контроллеров.
 * <p>
 * Обрабатывает некорректный JSON в запросах и возвращает корректный HTTP-ответ.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Обрабатывает ошибки парсинга JSON.
     *
     * @param ex исключение HttpMessageNotReadableException
     * @return ответ с HTTP 400 и сообщением об ошибке
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleInvalidJson(HttpMessageNotReadableException ex) {
        return ResponseEntity.badRequest().body("Invalid JSON format: " + ex.getMessage());
    }
}
