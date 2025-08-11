package com.bank.api.exception;


import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleInvalidJson(HttpMessageNotReadableException ex) {
        // Можно вернуть тело с описанием ошибки, или просто статус 400
        return ResponseEntity.badRequest().body("Invalid JSON format: " + ex.getMessage());
    }

    // Можно добавить другие обработчики, например, для MethodArgumentNotValidException, если используешь @Valid
}
