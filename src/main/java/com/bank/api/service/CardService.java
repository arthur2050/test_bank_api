package com.bank.api.service;

import com.bank.api.dto.CardDto;
import com.bank.api.dto.TransferRequestDto;
import com.bank.api.entity.*;
import com.bank.api.repository.*;
import com.bank.api.util.CardUtil;
import com.bank.api.util.CardValidator;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CardService implements CardServiceInterface {

    private final CardRepository cardRepository;
    private final UserRepository userRepository;
    private final TransferRepository transferRepository;

    public CardService(CardRepository cardRepository,
                       UserRepository userRepository,
                       TransferRepository transferRepository) {
        this.cardRepository = cardRepository;
        this.userRepository = userRepository;
        this.transferRepository = transferRepository;
    }

    @Override
    public List<CardDto> getAllCards() {
        return cardRepository.findAll()
                .stream()
                .map(CardDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public CardDto createCardForUser(String username, CardDto cardDto) {
        User user = getUserByUsername(username);

        Card card = new Card();
        card.setNumber(CardUtil.generateCardNumber());
        card.setOwner(user);
        card.setExpirationDate(cardDto.getExpirationDate());
        card.setStatus(CardUtil.determineInitialStatus(cardDto.getExpirationDate()));
        card.setBalance(CardUtil.defaultBalance(cardDto.getBalance()));

        cardRepository.save(card);
        return CardDto.fromEntity(card);
    }

    @Override
    public void blockCard(Long cardId) {
        updateCardStatus(cardId, CardStatus.BLOCKED);
    }

    @Override
    public void activateCard(Long cardId) {
        Card card = getCardOrThrow(cardId);
        CardValidator.validateNotExpired(card);
        updateCardStatus(card, CardStatus.ACTIVE);
    }

    @Override
    public void deleteCard(Long cardId) {
        cardRepository.deleteById(cardId);
    }

    @Override
    public Page<CardDto> getUserCards(String username, String status, Pageable pageable) {
        User user = getUserByUsername(username);

        if (status == null || status.isBlank()) {
            return cardRepository.findAllByOwner(user, pageable).map(CardDto::fromEntity);
        }

        CardStatus cardStatus = parseCardStatus(status);
        return cardRepository.findAllByOwnerAndStatus(user, cardStatus, pageable).map(CardDto::fromEntity);
    }

    @Override
    public void requestBlockCard(String username, Long cardId) {
        Card card = getCardOwnedByUser(username, cardId);
        if (card.getStatus() == CardStatus.BLOCKED) {
            throw new RuntimeException("Card already blocked");
        }
        updateCardStatus(card, CardStatus.BLOCKED);
    }

    @Override
    @Transactional
    public void transferBetweenCards(String username, TransferRequestDto transferRequest) {
        CardValidator.validatePositiveAmount(transferRequest.getAmount());

        Card fromCard = getCardOwnedByUser(username, transferRequest.getFromCardId());
        Card toCard = getCardOwnedByUser(username, transferRequest.getToCardId());

        CardValidator.validateActiveAndNotExpired(fromCard, "Source card");
        CardValidator.validateActiveAndNotExpired(toCard, "Destination card");
        CardValidator.validateSufficientBalance(fromCard, transferRequest.getAmount());

        performTransfer(fromCard, toCard, transferRequest.getAmount());
    }

    @Override
    public Double getCardBalance(String username, Long cardId) {
        Card card = getCardOwnedByUser(username, cardId);
        return card.getBalance().doubleValue();
    }

    // ----------------- хелперы -----------------

    private User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private Card getCardOrThrow(Long id) {
        return cardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Card not found"));
    }

    private Card getCardOwnedByUser(String username, Long cardId) {
        Card card = getCardOrThrow(cardId);
        if (!card.getOwner().getUsername().equals(username)) {
            throw new RuntimeException("Access denied: Card does not belong to user");
        }
        return card;
    }

    private void updateCardStatus(Long cardId, CardStatus status) {
        Card card = getCardOrThrow(cardId);
        updateCardStatus(card, status);
    }

    private void updateCardStatus(Card card, CardStatus status) {
        card.setStatus(status);
        cardRepository.save(card);
    }

    private CardStatus parseCardStatus(String status) {
        try {
            return CardStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid card status");
        }
    }

    private void performTransfer(Card fromCard, Card toCard, BigDecimal amount) {
        fromCard.setBalance(fromCard.getBalance().subtract(amount));
        toCard.setBalance(toCard.getBalance().add(amount));

        cardRepository.save(fromCard);
        cardRepository.save(toCard);

        Transfer transfer = new Transfer();
        transfer.setFromCard(fromCard);
        transfer.setToCard(toCard);
        transfer.setAmount(amount);
        transferRepository.save(transfer);
    }
}
