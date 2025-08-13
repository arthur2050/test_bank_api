package com.bank.api.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Глобальный обработчик исключений для REST-контроллеров.
 * <p>
 * Обрабатывает:
 * <ul>
 *     <li>Некорректный JSON ({@link HttpMessageNotReadableException})</li>
 *     <li>Ошибки десериализации enum ({@link InvalidFormatException})</li>
 *     <li>Кастомные бизнес-исключения ({@link BusinessException})</li>
 *     <li>Любые непредвиденные ошибки ({@link Exception})</li>
 * </ul>
 * <p>
 * Для продакшена сообщения ошибок сокращены и безопасны. В dev режиме показываются подробные сообщения.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /** Флаг продакшн режима, берётся из application.properties */
    @Value("${app.env.prod:true}")
    private boolean isProd;

    /**
     * Создаёт стандартизированный JSON-ответ для ошибок.
     *
     * @param status  HTTP-статус
     * @param error   краткое описание ошибки
     * @param message подробное сообщение
     * @param path    URI запроса
     * @return {@link ResponseEntity} с телом ошибки
     */
    private ResponseEntity<Map<String, Object>> buildResponse(HttpStatus status, String error, String message, String path) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", error);
        body.put("message", message);
        body.put("path", path);
        return new ResponseEntity<>(body, status);
    }

    /**
     * Обрабатывает некорректный JSON.
     *
     * @param ex      исключение {@link HttpMessageNotReadableException}
     * @param request объект {@link HttpServletRequest}
     * @return ответ с HTTP 400
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidJson(HttpMessageNotReadableException ex,
                                                                 HttpServletRequest request) {
        String message = isProd ? "Malformed request body" :
                ex.getMostSpecificCause() != null ? ex.getMostSpecificCause().getMessage() : ex.getMessage();
        return buildResponse(HttpStatus.BAD_REQUEST, "Invalid JSON format", message, request.getRequestURI());
    }

    /**
     * Обрабатывает ошибки неверного значения enum.
     *
     * @param ex      исключение {@link InvalidFormatException}
     * @param request объект {@link HttpServletRequest}
     * @return ответ с HTTP 400
     */
    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidEnum(InvalidFormatException ex,
                                                                 HttpServletRequest request) {
        String message;
        if (isProd) {
            message = "Invalid value provided";
        } else if (ex.getTargetType() != null && ex.getTargetType().isEnum()) {
            String allowedValues = String.join(", ",
                    Arrays.stream(ex.getTargetType().getEnumConstants())
                            .map(Object::toString)
                            .toArray(String[]::new)
            );
            message = String.format("Invalid value '%s' for enum %s. Allowed values: [%s]",
                    ex.getValue(), ex.getTargetType().getSimpleName(), allowedValues);
        } else {
            message = ex.getMessage();
        }
        return buildResponse(HttpStatus.BAD_REQUEST, "Invalid enum value", message, request.getRequestURI());
    }

    /**
     * Обрабатывает кастомные бизнес-исключения.
     *
     * @param ex      исключение {@link BusinessException}
     * @param request объект {@link HttpServletRequest}
     * @return ответ с HTTP 400
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Map<String, Object>> handleBusinessException(BusinessException ex,
                                                                       HttpServletRequest request) {
        String message = isProd ? "Business rule violation" : ex.getMessage();
        return buildResponse(HttpStatus.BAD_REQUEST, "Business error", message, request.getRequestURI());
    }

    /**
     * Обрабатывает любые непредвиденные ошибки.
     *
     * @param ex      исключение {@link Exception}
     * @param request объект {@link HttpServletRequest}
     * @return ответ с HTTP 500
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex,
                                                                      HttpServletRequest request) {
        String message = isProd ? "An unexpected error occurred" : ex.getMessage();
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error", message, request.getRequestURI());
    }
}
