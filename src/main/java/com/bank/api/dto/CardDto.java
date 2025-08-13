package com.bank.api.dto;


import com.bank.api.entity.Card;
import com.bank.api.entity.CardStatus;
import java.math.BigDecimal;
import java.time.LocalDate;

public class CardDto {
    private Long id;
    private String maskedNumber;
    private LocalDate expirationDate;
    private CardStatus status;
    private BigDecimal balance;

    public CardDto(Long id, String maskedNumber, LocalDate expirationDate, CardStatus status, BigDecimal balance) {
        this.id = id;
        this.maskedNumber = maskedNumber;
        this.expirationDate = expirationDate;
        this.status = status;
        this.balance = balance;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMaskedNumber() {
        return maskedNumber;
    }

    public void setMaskedNumber(String maskedNumber) {
        this.maskedNumber = maskedNumber;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public CardStatus getStatus() {
        return status;
    }

    public void setStatus(CardStatus status) {
        this.status = status;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public static CardDto fromEntity(Card card) {
        String maskedNumber = "**** **** **** " + card.getNumber().substring(card.getNumber().length() - 4);

        return new CardDto(
                card.getId(),
                maskedNumber,
                card.getExpirationDate(),
                card.getStatus(),
                card.getBalance()
        );
    }
}
