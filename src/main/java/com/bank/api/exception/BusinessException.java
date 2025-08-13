package com.bank.api.exception;

/**
 * Кастомное исключение для бизнес-логики.
 * <p>
 * Используется в сервисах для корректного информирования контроллеров и клиента о нарушениях бизнес-правил.
 */
public class BusinessException extends RuntimeException {

    public BusinessException() {
        super();
    }

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    public BusinessException(Throwable cause) {
        super(cause);
    }
}
