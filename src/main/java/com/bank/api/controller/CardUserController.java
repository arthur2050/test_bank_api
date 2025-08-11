package com.bank.api.controller;

import com.bank.api.dto.CardDto;
import com.bank.api.dto.TransferRequestDto;
import com.bank.api.service.CardServiceInterface;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/card")
public class CardUserController {

    private final CardServiceInterface cardService;

    public CardUserController(CardServiceInterface cardService) {
        this.cardService = cardService;
    }

    // Получить страницы карт пользователя с возможностью фильтрации (по статусу, например)
    @GetMapping
    public Page<CardDto> getUserCards(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return cardService.getUserCards(userDetails.getUsername(), status, PageRequest.of(page, size));
    }

    // Запросить блокировку карты
    @PatchMapping("/{cardId}/block")
    public void requestBlockCard(@AuthenticationPrincipal UserDetails userDetails,
                                 @PathVariable Long cardId) {
        cardService.requestBlockCard(userDetails.getUsername(), cardId);
    }

    // Перевод между своими картами
    @PostMapping("/transfer")
    public void transferBetweenCards(@AuthenticationPrincipal UserDetails userDetails,
                                     @RequestBody TransferRequestDto transferRequest) {
        cardService.transferBetweenCards(userDetails.getUsername(), transferRequest);
    }

    // Получить баланс по карте
    @GetMapping("/{cardId}/balance")
    public Double getCardBalance(@AuthenticationPrincipal UserDetails userDetails,
                                 @PathVariable Long cardId) {
        return cardService.getCardBalance(userDetails.getUsername(), cardId);
    }
}
