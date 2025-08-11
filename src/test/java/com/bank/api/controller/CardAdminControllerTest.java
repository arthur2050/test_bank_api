package com.bank.api.controller;

import com.bank.api.dto.CardDto;
import com.bank.api.entity.CardStatus;
import com.bank.api.service.CardService;

import com.bank.api.util.JwtFilter;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = CardAdminController.class, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtFilter.class)
})
public class CardAdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CardService cardService; // мок вместо настоящего бина

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testGetAllCards() throws Exception {
        CardDto card1 = new CardDto(
                1L,
                "**** **** **** 1234",
                LocalDate.of(2026, 12, 31),
                CardStatus.ACTIVE,
                new BigDecimal("100.00")
        );

        CardDto card2 = new CardDto(
                2L,
                "**** **** **** 5678",
                LocalDate.of(2027, 6, 30),
                CardStatus.BLOCKED,
                new BigDecimal("50.00")
        );

        when(cardService.getAllCards()).thenReturn(List.of(card1, card2));

        mockMvc.perform(get("/api/admin/cards"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].maskedNumber").value("**** **** **** 1234"));
    }


    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testCreateCardForUser() throws Exception {
        CardDto requestCard = new CardDto(null, null, LocalDate.of(2026, 12, 31), CardStatus.ACTIVE, new BigDecimal("100.00"));
        CardDto responseCard = new CardDto(1L, "**** **** **** 9999", LocalDate.of(2026, 12, 31), CardStatus.ACTIVE, new BigDecimal("100.00"));

        when(cardService.createCardForUser(eq("john"), any(CardDto.class))).thenReturn(responseCard);

        String jsonBody = """
                {
                  "expirationDate": "2026-12-31",
                  "status": "ACTIVE",
                  "balance": 100.00
                }
                """;

        mockMvc.perform(post("/api/admin/card").with(SecurityMockMvcRequestPostProcessors.csrf())
                        .param("username", "john")
                        .contentType("application/json")
                        .content(jsonBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.maskedNumber").value("**** **** **** 9999"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testBlockCard() throws Exception {
        doNothing().when(cardService).blockCard(1L);

        mockMvc.perform(patch("/api/admin/card/1/block").with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk());
        verify(cardService).blockCard(1L);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testActivateCard() throws Exception {
        doNothing().when(cardService).activateCard(1L);

        mockMvc.perform(patch("/api/admin/card/1/activate").with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk());
        verify(cardService).activateCard(1L);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testDeleteCard() throws Exception {
        doNothing().when(cardService).deleteCard(1L);

        mockMvc.perform(delete("/api/admin/card/1").with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk());
        verify(cardService).deleteCard(1L);
    }
}
