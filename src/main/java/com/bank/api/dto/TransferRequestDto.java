package com.bank.api.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransferRequestDto {
    private Long fromCardId;
    private Long toCardId;
    private BigDecimal amount;
}
