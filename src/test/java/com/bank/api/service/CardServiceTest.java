package com.bank.api.service;

import com.bank.api.dto.CardDto;
import com.bank.api.dto.TransferRequestDto;
import com.bank.api.entity.Card;
import com.bank.api.entity.CardStatus;
import com.bank.api.entity.User;
import com.bank.api.repository.CardRepository;
import com.bank.api.repository.TransferRepository;
import com.bank.api.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CardServiceTest {

    private CardService cardService;
    private CardRepository cardRepository;
    private UserRepository userRepository;
    private TransferRepository transferRepository;
    private User user;

    @BeforeEach
    void setUp() {
        cardRepository = mock(CardRepository.class);
        userRepository = mock(UserRepository.class);
        transferRepository = mock(TransferRepository.class);

        cardService = new CardService(cardRepository, userRepository, transferRepository);

        // Создаем фиктивного пользователя
        user = new User();
        user.setUsername("john");
        when(userRepository.findByUsername("john")).thenReturn(Optional.of(user));
    }

    @Test
    void createCardForUser_returnsDto() {
        CardDto dto = new CardDto(
                null, null, LocalDate.now().plusYears(1),
                CardStatus.ACTIVE, new BigDecimal("100.00")
        );

        CardDto responseDto = new CardDto(
                1L, "**** **** **** 1234",
                dto.getExpirationDate(), dto.getStatus(), dto.getBalance()
        );

        when(cardRepository.save(any(Card.class))).thenAnswer(invocation -> {
            Card card = invocation.getArgument(0);
            card.setNumber("1234567890123456"); // генерируем номер
            return card;
        });

        CardDto created = cardService.createCardForUser("john", dto);

        assertNotNull(created);
        assertEquals(dto.getBalance(), created.getBalance());
        assertEquals(CardStatus.ACTIVE, created.getStatus());
        assertNotNull(created.getMaskedNumber());
    }

    @Test
    void transferBetweenCards_success() {
        Card fromCard = new Card();
        fromCard.setOwner(user);
        fromCard.setBalance(new BigDecimal("200"));
        fromCard.setStatus(CardStatus.ACTIVE);
        fromCard.setNumber("1234567890123456");
        fromCard.setExpirationDate(LocalDate.now().plusYears(1));

        Card toCard = new Card();
        toCard.setOwner(user);
        toCard.setBalance(new BigDecimal("50"));
        toCard.setStatus(CardStatus.ACTIVE);
        toCard.setNumber("9876543210987654");
        toCard.setExpirationDate(LocalDate.now().plusYears(1));

        when(cardRepository.findById(1L)).thenReturn(Optional.of(fromCard));
        when(cardRepository.findById(2L)).thenReturn(Optional.of(toCard));

        TransferRequestDto dto = new TransferRequestDto();
        dto.setFromCardId(1L);
        dto.setToCardId(2L);
        dto.setAmount(new BigDecimal("100"));

        cardService.transferBetweenCards("john", dto);

        assertEquals(new BigDecimal("100"), fromCard.getBalance());
        assertEquals(new BigDecimal("150"), toCard.getBalance());
        verify(cardRepository, times(2)).save(any(Card.class));
        verify(transferRepository).save(any());
    }

    @Test
    void transferBetweenCards_insufficientBalance_throws() {
        Card fromCard = new Card();
        fromCard.setOwner(user);
        fromCard.setBalance(new BigDecimal("50"));
        fromCard.setStatus(CardStatus.ACTIVE);
        fromCard.setNumber("1234567890123456");
        fromCard.setExpirationDate(LocalDate.now().plusYears(1));

        Card toCard = new Card();
        toCard.setOwner(user);
        toCard.setBalance(new BigDecimal("50"));
        toCard.setStatus(CardStatus.ACTIVE);
        toCard.setNumber("9876543210987654");
        toCard.setExpirationDate(LocalDate.now().plusYears(1));

        when(cardRepository.findById(1L)).thenReturn(Optional.of(fromCard));
        when(cardRepository.findById(2L)).thenReturn(Optional.of(toCard));

        TransferRequestDto dto = new TransferRequestDto();
        dto.setFromCardId(1L);
        dto.setToCardId(2L);
        dto.setAmount(new BigDecimal("100"));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> cardService.transferBetweenCards("john", dto));
        assertTrue(ex.getMessage().toLowerCase().contains("balance"));
    }

    @Test
    void blockCard_invokesService() {
        Card card = new Card();
        card.setOwner(user);
        card.setStatus(CardStatus.ACTIVE);
        card.setNumber("1234567890123456");
        card.setExpirationDate(LocalDate.now().plusYears(1));

        when(cardRepository.findById(1L)).thenReturn(Optional.of(card));

        cardService.blockCard(1L);

        assertEquals(CardStatus.BLOCKED, card.getStatus());
        verify(cardRepository).save(card);
    }

    @Test
    void activateCard_invokesService() {
        Card card = new Card();
        card.setOwner(user);
        card.setStatus(CardStatus.BLOCKED);
        card.setNumber("1234567890123456");
        card.setExpirationDate(LocalDate.now().plusYears(1));

        when(cardRepository.findById(1L)).thenReturn(Optional.of(card));

        cardService.activateCard(1L);

        assertEquals(CardStatus.ACTIVE, card.getStatus());
        verify(cardRepository).save(card);
    }

    @Test
    void getUserCards_withStatus_returnsFiltered() {
        Card card = new Card();
        card.setOwner(user);
        card.setStatus(CardStatus.ACTIVE);
        card.setNumber("1234567890123456");
        card.setExpirationDate(LocalDate.now().plusYears(1));

        when(cardRepository.findAllByOwnerAndStatus(eq(user), eq(CardStatus.ACTIVE), any()))
                .thenReturn(new PageImpl<>(List.of(card)));

        var page = cardService.getUserCards("john", "ACTIVE", Pageable.unpaged());
        assertEquals(1, page.getContent().size());
        assertEquals(CardStatus.ACTIVE, page.getContent().get(0).getStatus());
    }

    @Test
    void getUserCards_withoutStatus_returnsAll() {
        Card card = new Card();
        card.setOwner(user);
        card.setStatus(CardStatus.ACTIVE);
        card.setNumber("1234567890123456");
        card.setExpirationDate(LocalDate.now().plusYears(1));

        when(cardRepository.findAllByOwner(eq(user), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(card)));

        var page = cardService.getUserCards("john", null, Pageable.unpaged());
        assertEquals(1, page.getContent().size());
    }
}
