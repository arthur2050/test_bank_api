package com.bank.api.util;

import com.bank.api.entity.Card;
import com.bank.api.entity.CardStatus;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class CardValidatorTest {

    @Test
    void validateNotExpired_activeCard_passes() {
        Card card = new Card();
        card.setExpirationDate(LocalDate.now().plusDays(1));
        card.setStatus(CardStatus.ACTIVE);

        assertDoesNotThrow(() -> CardValidator.validateNotExpired(card));
    }

    @Test
    void validateNotExpired_expiredCard_throws() {
        Card card = new Card();
        card.setExpirationDate(LocalDate.now().minusDays(1));
        card.setStatus(CardStatus.ACTIVE);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> CardValidator.validateNotExpired(card));
        assertEquals("Card expired", ex.getMessage());
        assertEquals(CardStatus.EXPIRED, card.getStatus());
    }

    @Test
    void validateActiveAndNotExpired_inactiveCard_throws() {
        Card card = new Card();
        card.setExpirationDate(LocalDate.now().plusDays(1));
        card.setStatus(CardStatus.BLOCKED);

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                CardValidator.validateActiveAndNotExpired(card, "Card"));
        assertTrue(ex.getMessage().contains("is not active"));
    }

    @Test
    void validatePositiveAmount_negativeAmount_throws() {
        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                CardValidator.validatePositiveAmount(new BigDecimal("-10")));
        assertEquals("Amount must be positive", ex.getMessage());
    }

    @Test
    void validateSufficientBalance_insufficient_throws() {
        Card card = new Card();
        card.setBalance(new BigDecimal("50"));

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                CardValidator.validateSufficientBalance(card, new BigDecimal("100")));
        assertEquals("Insufficient balance", ex.getMessage());
    }
}
