package com.bank.api.controller;

import com.bank.api.dto.CardDto;
import com.bank.api.service.CardService;
import com.bank.api.service.CardServiceInterface;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class CardAdminController {

    private final CardServiceInterface cardService;

    public CardAdminController(CardServiceInterface cardService) {
        this.cardService = cardService;
    }

    @GetMapping("/cards")
    public List<CardDto> getAllCards() {
        return cardService.getAllCards();
    }

    @PostMapping("/card")
    public CardDto createCardForUser(@RequestParam String username, @RequestBody CardDto cardDto) {
        return cardService.createCardForUser(username, cardDto);
    }

    @PatchMapping("/card/{cardId}/block")
    public void blockCard(@PathVariable Long cardId) {
        cardService.blockCard(cardId);
    }

    @PatchMapping("/card/{cardId}/activate")
    public void activateCard(@PathVariable Long cardId) {
        cardService.activateCard(cardId);
    }

    // Удаление карты
    @DeleteMapping("/card/{cardId}")
    public void deleteCard(@PathVariable Long cardId) {
        cardService.deleteCard(cardId);
    }

}
