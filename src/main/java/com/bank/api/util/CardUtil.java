package com.bank.api.util;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.bank.api.entity.CardStatus;

/**
 * Вспомогательный класс для работы с банковскими картами.
 * <p>
 * Генерация номера карты, установка начального баланса и определение статуса.
 */
public class CardUtil {
    private CardUtil() {}

    /** Генерация случайного 16-значного номера карты */
    public static String generateCardNumber() {
        return IntStream.range(0, 16)
                .mapToObj(i -> String.valueOf((int) (Math.random() * 10)))
                .collect(Collectors.joining());
    }

    /** Возвращает баланс карты, если не указан, устанавливает 0 */
    public static BigDecimal defaultBalance(BigDecimal balance) {
        return balance != null ? balance : BigDecimal.ZERO;
    }

    /** Определяет начальный статус карты по дате истечения */
    public static CardStatus determineInitialStatus(LocalDate expirationDate) {
        return (expirationDate == null || expirationDate.isBefore(LocalDate.now()))
                ? CardStatus.EXPIRED
                : CardStatus.ACTIVE;
    }

    /** Проверяет, истекла ли карта */
    public static boolean isCardExpired(LocalDate expirationDate) {
        return expirationDate != null && expirationDate.isBefore(LocalDate.now());
    }

    /** Проверяет, валидна ли карта */
    public static boolean isCardValid(LocalDate expirationDate) {
        return expirationDate != null && !isCardExpired(expirationDate);
    }
}
