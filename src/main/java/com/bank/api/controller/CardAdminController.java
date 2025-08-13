package com.bank.api.controller;

import com.bank.api.dto.CardDto;
import com.bank.api.service.CardService;
import com.bank.api.service.CardServiceInterface;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST-контроллер для управления банковскими картами администраторами.
 * <p>
 * Предоставляет эндпоинты для получения списка карт, создания карты для пользователя,
 * блокировки, активации и удаления карты.
 * <p>
 * Все операции выполняются с правами администратора.
 * Базовый URL: /api/admin
 */
@RestController
@RequestMapping("/api/admin")
public class CardAdminController {

    private final CardServiceInterface cardService;

    public CardAdminController(CardServiceInterface cardService) {
        this.cardService = cardService;
    }

    /**
     * Возвращает список всех карт в системе.
     *
     * @return список DTO карт
     */
    @GetMapping("/cards")
    public List<CardDto> getAllCards() {
        return cardService.getAllCards();
    }

    /**
     * Создает карту для указанного пользователя.
     *
     * @param username логин пользователя, для которого создается карта
     * @param cardDto  DTO с данными карты (срок действия, баланс)
     * @return DTO созданной карты
     */
    @PostMapping("/card")
    public CardDto createCardForUser(@RequestParam String username, @RequestBody CardDto cardDto) {
        return cardService.createCardForUser(username, cardDto);
    }


    /**
     * Блокирует карту по ID.
     *
     * @param cardId ID карты
     */
    @PatchMapping("/card/{cardId}/block")
    public void blockCard(@PathVariable Long cardId) {
        cardService.blockCard(cardId);
    }

    /**
     * Активирует карту по ID.
     *
     * @param cardId ID карты
     */
    @PatchMapping("/card/{cardId}/activate")
    public void activateCard(@PathVariable Long cardId) {
        cardService.activateCard(cardId);
    }

    /**
     * Удаляет карту по ID.
     *
     * @param cardId ID карты
     */
    @DeleteMapping("/card/{cardId}")
    public void deleteCard(@PathVariable Long cardId) {
        cardService.deleteCard(cardId);
    }

}
