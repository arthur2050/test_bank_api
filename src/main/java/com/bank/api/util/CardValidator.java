package com.bank.api.util;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.bank.api.entity.Card;
import com.bank.api.entity.CardStatus;

/**
 * Класс для валидации банковских карт.
 * <p>
 * Проверка баланса, статуса и даты истечения.
 */
public class CardValidator {

    private CardValidator() {}

    /** Проверяет, что карта не истекла */
    public static void validateNotExpired(Card card) {
        if (card.getExpirationDate().isBefore(LocalDate.now())) {
            card.setStatus(CardStatus.EXPIRED);
            throw new RuntimeException("Card expired");
        }
    }

    /** Проверяет, что карта активна и не истекла */
    public static void validateActiveAndNotExpired(Card card, String context) {
        if (card.getStatus() != CardStatus.ACTIVE) {
            throw new RuntimeException(context + " is not active");
        }
        validateNotExpired(card);
    }

    /** Проверяет, что сумма перевода положительна */
    public static void validatePositiveAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Amount must be positive");
        }
    }

    /** Проверяет, что на карте достаточно средств */
    public static void validateSufficientBalance(Card card, BigDecimal amount) {
        if (card.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient balance");
        }
    }
}
