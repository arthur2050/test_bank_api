package com.bank.api.controller;

import com.bank.api.dto.CardDto;
import com.bank.api.dto.TransferRequestDto;
import com.bank.api.service.CardServiceInterface;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

/**
 * Контроллер для пользователей для работы со своими банковскими картами.
 * <p>
 * Позволяет пользователю:
 * <ul>
 *     <li>Просматривать свои карты с фильтром по статусу и пагинацией</li>
 *     <li>Запрашивать блокировку карты</li>
 *     <li>Выполнять переводы между своими картами</li>
 *     <li>Получать текущий баланс карты</li>
 * </ul>
 * Базовый URL: /api/user/card
 */
@RestController
@RequestMapping("/api/user/card")
public class CardUserController {

    private final CardServiceInterface cardService;

    public CardUserController(CardServiceInterface cardService) {
        this.cardService = cardService;
    }

    /**
     * Возвращает список карт пользователя с возможностью фильтрации по статусу и пагинации.
     *
     * @param userDetails объект аутентифицированного пользователя
     * @param status      (необязательный) фильтр по статусу карты (ACTIVE, BLOCKED, EXPIRED)
     * @param page        номер страницы (по умолчанию 0)
     * @param size        размер страницы (по умолчанию 10)
     * @return страница DTO карт пользователя
     */
    @GetMapping
    public Page<CardDto> getUserCards(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return cardService.getUserCards(userDetails.getUsername(), status, PageRequest.of(page, size));
    }

    /**
     * Позволяет пользователю запросить блокировку своей карты.
     *
     * @param userDetails объект аутентифицированного пользователя
     * @param cardId      ID карты для блокировки
     */
    @PatchMapping("/{cardId}/block")
    public void requestBlockCard(@AuthenticationPrincipal UserDetails userDetails,
                                 @PathVariable Long cardId) {
        cardService.requestBlockCard(userDetails.getUsername(), cardId);
    }

    /**
     * Выполняет перевод между картами пользователя.
     *
     * @param userDetails     объект аутентифицированного пользователя
     * @param transferRequest DTO с данными перевода (fromCardId, toCardId, amount)
     */
    @PostMapping("/transfer")
    public void transferBetweenCards(@AuthenticationPrincipal UserDetails userDetails,
                                     @RequestBody TransferRequestDto transferRequest) {
        cardService.transferBetweenCards(userDetails.getUsername(), transferRequest);
    }

    /**
     * Возвращает текущий баланс указанной карты пользователя.
     *
     * @param userDetails объект аутентифицированного пользователя
     * @param cardId      ID карты
     * @return баланс карты в формате Double
     */
    @GetMapping("/{cardId}/balance")
    public Double getCardBalance(@AuthenticationPrincipal UserDetails userDetails,
                                 @PathVariable Long cardId) {
        return cardService.getCardBalance(userDetails.getUsername(), cardId);
    }
}
