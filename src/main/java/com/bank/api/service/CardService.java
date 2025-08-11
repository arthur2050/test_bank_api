package com.bank.api.service;

import com.bank.api.dto.CardDto;
import com.bank.api.dto.TransferRequestDto;
import com.bank.api.entity.Card;
import com.bank.api.entity.CardStatus;
import com.bank.api.entity.Transfer;
import com.bank.api.entity.User;
import com.bank.api.repository.CardRepository;
import com.bank.api.repository.TransferRepository;
import com.bank.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import java.time.LocalDate;


@Service
public class CardService implements CardServiceInterface {
    @Autowired private CardRepository cardRepository;
    @Autowired private UserRepository userRepository;
    private final TransferRepository transferRepository;
    public CardService(TransferRepository transferRepository) {
        this.transferRepository = transferRepository;
    }

    private CardDto convertToDto(Card card) {
        return CardDto.fromEntity(card);
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
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Card card = new Card();
        // Создаем случайный номер карты, например (для упрощения)
        card.setNumber(generateCardNumber());
        card.setOwner(user);
        card.setExpirationDate(cardDto.getExpirationDate());

        if (cardDto.getExpirationDate() == null || cardDto.getExpirationDate().isBefore(LocalDate.now())) {
            card.setStatus(CardStatus.EXPIRED);
        } else {
            card.setStatus(CardStatus.ACTIVE);
        }
        card.setBalance(cardDto.getBalance() != null ? cardDto.getBalance() : BigDecimal.ZERO);

        cardRepository.save(card);
        return CardDto.fromEntity(card);
    }

    private String generateCardNumber() {
        // Пример генерации 16-значного номера карты
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            sb.append((int) (Math.random() * 10));
        }
        return sb.toString();
    }

    public void blockCard(Long cardId) {
        Card card = getCardOrThrow(cardId);
        card.setStatus(CardStatus.BLOCKED);
        cardRepository.save(card);
    }

    @Override
    public void activateCard(Long cardId) {
        Card card = getCardOrThrow(cardId);
        if (card.getExpirationDate().isBefore(LocalDate.now())) {
            throw new RuntimeException("Card expired and cannot be activated");
        }
        card.setStatus(CardStatus.ACTIVE);
        cardRepository.save(card);
    }

    public void deleteCard(Long cardId) {
        cardRepository.deleteById(cardId);
    }

    @Override
    public Page<CardDto> getUserCards(String username, String status, Pageable pageable) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Page<Card> cardsPage;

        if (status == null || status.isBlank()) {
            cardsPage = cardRepository.findAllByOwner(user, pageable);
        } else {
            CardStatus cardStatus;
            try {
                cardStatus = CardStatus.valueOf(status.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Invalid card status");
            }
            cardsPage = cardRepository.findAllByOwnerAndStatus(user, cardStatus, pageable);
        }

        return cardsPage.map(CardDto::fromEntity);
    }

    @Override
    public void requestBlockCard(String username, Long cardId) {
        Card card = getCardOrThrow(cardId);

        if (!card.getOwner().getUsername().equals(username)) {
            throw new RuntimeException("Access denied: Card does not belong to user");
        }

        if (card.getStatus() == CardStatus.BLOCKED) {
            throw new RuntimeException("Card already blocked");
        }

        card.setStatus(CardStatus.BLOCKED);
        cardRepository.save(card);
    }

    @Override
    @Transactional
    public void transferBetweenCards(String username, TransferRequestDto transferRequest) {
        if (transferRequest.getAmount() == null || transferRequest.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Amount must be positive");
        }

        Card fromCard = getCardOrThrow(transferRequest.getFromCardId());
        Card toCard = getCardOrThrow(transferRequest.getToCardId());

        if (!fromCard.getOwner().getUsername().equals(username) || !toCard.getOwner().getUsername().equals(username)) {
            throw new RuntimeException("Transfer allowed only between user's own cards");
        }

        if (fromCard.getStatus() != CardStatus.ACTIVE) {
            throw new RuntimeException("Source card is not active");
        }

        if (toCard.getStatus() != CardStatus.ACTIVE) {
            throw new RuntimeException("Destination card is not active");
        }

        if (fromCard.getExpirationDate().isBefore(LocalDate.now())) {
            fromCard.setStatus(CardStatus.EXPIRED);
            cardRepository.save(fromCard);
            throw new RuntimeException("Source card is expired");
        }

        if (toCard.getExpirationDate().isBefore(LocalDate.now())) {
            toCard.setStatus(CardStatus.EXPIRED);
            cardRepository.save(toCard);
            throw new RuntimeException("Destination card is expired");
        }

        if (fromCard.getBalance().compareTo(transferRequest.getAmount()) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        fromCard.setBalance(fromCard.getBalance().subtract(transferRequest.getAmount()));
        toCard.setBalance(toCard.getBalance().add(transferRequest.getAmount()));

        cardRepository.save(fromCard);
        cardRepository.save(toCard);

        Transfer transfer = new Transfer();
        transfer.setFromCard(fromCard);
        transfer.setToCard(toCard);
        transfer.setAmount(transferRequest.getAmount());
        transferRepository.save(transfer);
    }

    @Override
    public Double getCardBalance(String username, Long cardId) {
        Card card = getCardOrThrow(cardId);

        if (!card.getOwner().getUsername().equals(username)) {
            throw new RuntimeException("Access denied: Card does not belong to user");
        }

        return card.getBalance().doubleValue();
    }

    private Card getCardOrThrow(Long id) {
        return cardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Card not found"));
    }
}
