package com.bank.api.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * DTO для перевода между картами.
 * <p>
 * Содержит ID карты отправителя, ID карты получателя и сумму перевода.
 */
@Data
public class TransferRequestDto {
    private Long fromCardId;
    private Long toCardId;
    private BigDecimal amount;
}
