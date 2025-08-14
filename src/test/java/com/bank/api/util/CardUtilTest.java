package com.bank.api.util;

import com.bank.api.entity.CardStatus;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class CardUtilTest {

    @Test
    void generateCardNumber_returns16Digits() {
        String number = CardUtil.generateCardNumber();
        assertNotNull(number);
        assertEquals(16, number.length());
        assertTrue(number.chars().allMatch(Character::isDigit));
    }

    @Test
    void defaultBalance_returnsBalanceOrZero() {
        assertEquals(new BigDecimal("100"), CardUtil.defaultBalance(new BigDecimal("100")));
        assertEquals(BigDecimal.ZERO, CardUtil.defaultBalance(null));
    }

    @Test
    void determineInitialStatus_returnsActiveOrExpired() {
        assertEquals(CardStatus.ACTIVE, CardUtil.determineInitialStatus(LocalDate.now().plusDays(1)));
        assertEquals(CardStatus.EXPIRED, CardUtil.determineInitialStatus(LocalDate.now().minusDays(1)));
        assertEquals(CardStatus.EXPIRED, CardUtil.determineInitialStatus(null));
    }

    @Test
    void isCardExpired_returnsCorrectly() {
        assertTrue(CardUtil.isCardExpired(LocalDate.now().minusDays(1)));
        assertFalse(CardUtil.isCardExpired(LocalDate.now().plusDays(1)));
        assertFalse(CardUtil.isCardExpired(null));
    }

    @Test
    void isCardValid_returnsCorrectly() {
        assertTrue(CardUtil.isCardValid(LocalDate.now().plusDays(1)));
        assertFalse(CardUtil.isCardValid(LocalDate.now().minusDays(1)));
        assertFalse(CardUtil.isCardValid(null));
    }
}
