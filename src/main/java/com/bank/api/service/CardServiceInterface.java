package com.bank.api.service;

import com.bank.api.dto.CardDto;
import com.bank.api.dto.TransferRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CardServiceInterface {
    List<CardDto> getAllCards();

    CardDto createCardForUser(String username, CardDto cardDto);

    void blockCard(Long cardId);

    void activateCard(Long cardId);

    void deleteCard(Long cardId);

    // Новый функционал для пользователей:
    Page<CardDto> getUserCards(String username, String status, Pageable pageable);

    void requestBlockCard(String username, Long cardId);

    void transferBetweenCards(String username, TransferRequestDto transferRequest);

    Double getCardBalance(String username, Long cardId);
}
