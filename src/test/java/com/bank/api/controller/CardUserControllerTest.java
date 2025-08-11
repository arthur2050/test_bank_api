package com.bank.api.controller;

import com.bank.api.dto.CardDto;
import com.bank.api.dto.TransferRequestDto;
import com.bank.api.entity.CardStatus;
import com.bank.api.service.CardService;
import com.bank.api.util.JwtFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = CardUserController.class, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtFilter.class)
})
public class CardUserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CardService cardService;

    @Test
    @WithMockUser(username = "john")
    void testGetUserCards() throws Exception {
        CardDto card = new CardDto(1L, "**** **** **** 1234", LocalDate.of(2026, 12, 31), CardStatus.ACTIVE, new BigDecimal("100.00"));

        when(cardService.getUserCards(eq("john"), isNull(), any(PageRequest.class)))
                .thenReturn(new PageImpl<>(List.of(card)));

        mockMvc.perform(get("/api/user/card"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].maskedNumber").value("**** **** **** 1234"));
    }

    @Test
    @WithMockUser(username = "john")
    void testRequestBlockCard() throws Exception {
        doNothing().when(cardService).requestBlockCard("john", 1L);

        mockMvc.perform(patch("/api/user/card/1/block").with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk());
        verify(cardService).requestBlockCard("john", 1L);
    }

    @Test
    @WithMockUser(username = "john")
    void testTransferBetweenCards() throws Exception {
        doNothing().when(cardService).transferBetweenCards(eq("john"), any(TransferRequestDto.class));

        String jsonBody = """
                {
                  "fromCardId": 1,
                  "toCardId": 2,
                  "amount": 50.00
                }
                """;

        mockMvc.perform(post("/api/user/card/transfer").with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType("application/json")
                        .content(jsonBody))
                .andExpect(status().isOk());
        verify(cardService).transferBetweenCards(eq("john"), any(TransferRequestDto.class));
    }

    @Test
    @WithMockUser(username = "john")
    void testGetCardBalance() throws Exception {
        when(cardService.getCardBalance("john", 1L)).thenReturn(100.0);

        mockMvc.perform(get("/api/user/card/1/balance").with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("100.0"));
    }
}
