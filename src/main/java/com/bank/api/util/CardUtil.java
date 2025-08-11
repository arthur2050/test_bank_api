package com.bank.api.util;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.bank.api.entity.CardStatus;

public class CardUtil {
    private CardUtil() {}

    public static String generateCardNumber() {
        return IntStream.range(0, 16)
                .mapToObj(i -> String.valueOf((int) (Math.random() * 10)))
                .collect(Collectors.joining());
    }

    public static BigDecimal defaultBalance(BigDecimal balance) {
        return balance != null ? balance : BigDecimal.ZERO;
    }

    public static CardStatus determineInitialStatus(LocalDate expirationDate) {
        return (expirationDate == null || expirationDate.isBefore(LocalDate.now()))
                ? CardStatus.EXPIRED
                : CardStatus.ACTIVE;
    }

    public static boolean isCardExpired(LocalDate expirationDate) {
        return expirationDate != null && expirationDate.isBefore(LocalDate.now());
    }

    public static boolean isCardValid(LocalDate expirationDate) {
        return expirationDate != null && !isCardExpired(expirationDate);
    }
}
