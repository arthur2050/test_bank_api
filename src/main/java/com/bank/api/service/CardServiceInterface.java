package com.bank.api.service;

import com.bank.api.dto.CardDto;
import com.bank.api.dto.TransferRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Интерфейс для сервиса работы с банковскими картами.
 */
public interface CardServiceInterface {
    /**
     * Возвращает все карты в системе.
     *
     * @return список DTO карт
     */
    List<CardDto> getAllCards();

    /**
     * Создает карту для пользователя.
     *
     * @param username логин пользователя
     * @param cardDto  данные карты
     * @return DTO созданной карты
     */
    CardDto createCardForUser(String username, CardDto cardDto);

    /**
     * Блокирует карту по ID.
     *
     * @param cardId ID карты
     */
    void blockCard(Long cardId);


    /**
     * Активирует карту по ID.
     *
     * @param cardId ID карты
     */
    void activateCard(Long cardId);

    /**
     * Удаляет карту по ID.
     *
     * @param cardId ID карты
     */
    void deleteCard(Long cardId);

    /**
     * Возвращает карты пользователя с фильтром по статусу и пагинацией.
     *
     * @param username логин пользователя
     * @param status   статус карты (опционально)
     * @param pageable объект пагинации
     * @return страница DTO карт
     */
    Page<CardDto> getUserCards(String username, String status, Pageable pageable);

    /**
     * Запрашивает блокировку карты пользователя.
     *
     * @param username логин пользователя
     * @param cardId   ID карты
     */
    void requestBlockCard(String username, Long cardId);

    /**
     * Переводит деньги между картами пользователя.
     *
     * @param username        логин пользователя
     * @param transferRequest DTO с данными перевода
     */
    void transferBetweenCards(String username, TransferRequestDto transferRequest);

    /**
     * Возвращает баланс карты пользователя.
     *
     * @param username логин пользователя
     * @param cardId   ID карты
     * @return баланс карты
     */
    Double getCardBalance(String username, Long cardId);
}
