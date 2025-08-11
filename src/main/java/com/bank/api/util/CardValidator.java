package com.bank.api.util;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.bank.api.entity.Card;
import com.bank.api.entity.CardStatus;

public class CardValidator {

    private CardValidator() {}

    public static void validateNotExpired(Card card) {
        if (card.getExpirationDate().isBefore(LocalDate.now())) {
            card.setStatus(CardStatus.EXPIRED);
            throw new RuntimeException("Card expired");
        }
    }

    public static void validateActiveAndNotExpired(Card card, String context) {
        if (card.getStatus() != CardStatus.ACTIVE) {
            throw new RuntimeException(context + " is not active");
        }
        validateNotExpired(card);
    }

    public static void validatePositiveAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Amount must be positive");
        }
    }

    public static void validateSufficientBalance(Card card, BigDecimal amount) {
        if (card.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient balance");
        }
    }
}
