package com.project.account.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.project.account.dto.DepositDTO;
import com.project.account.dto.TransferDTO;
import com.project.account.dto.WithdrawDTO;
import com.project.account.service.BalanceOperationService;
import com.project.account.service.TransferOperationService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BalanceOperationController.class)
class BalanceOperationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BalanceOperationService balanceOperationService;

    @MockBean
    private TransferOperationService transferOperationService;

    @Autowired
    private ObjectMapper objectMapper;

    @TestConfiguration
    static class TestConfig {

        @Bean
        public MessageConverter jacksonConverter() {
            return new Jackson2JsonMessageConverter(objectMapper());
        }

        @Bean
        public ObjectMapper objectMapper() {
            return new ObjectMapper().registerModule(new JavaTimeModule());
        }
    }

    @SneakyThrows
    @Test
    void depositAmountShouldWork() {
        DepositDTO depositDTO = new DepositDTO(1, 100, 1234);
        mockMvc.perform(post("/api/balance/deposit")
                .content(objectMapper.writeValueAsString(depositDTO))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pin").exists());
    }

    @SneakyThrows
    @Test
    void depositAmountBadRequest() {
        DepositDTO depositDTO = new DepositDTO(-1, -1, 12345);
        mockMvc.perform(post("/api/balance/deposit")
                .content(objectMapper.writeValueAsString(depositDTO))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.pin").doesNotExist());
    }

    @SneakyThrows
    @Test
    void withdrawAmountShouldWork() {
        WithdrawDTO withdrawDTO = new WithdrawDTO(1, 100, 1234);
        mockMvc.perform(post("/api/balance/withdraw")
                .content(objectMapper.writeValueAsString(withdrawDTO))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pin").exists());
    }

    @SneakyThrows
    @Test
    void withdrawAmountBadRequest() {
        WithdrawDTO withdrawDTO = new WithdrawDTO(-1, -1, 12345);
        mockMvc.perform(post("/api/balance/withdraw")
                .content(objectMapper.writeValueAsString(withdrawDTO))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.pin").doesNotExist());
    }

    @SneakyThrows
    @Test
    void transferAmountShouldWork() {
        TransferDTO withdrawDTO = new TransferDTO(1, 2, 100, 1234);
        mockMvc.perform(post("/api/balance/transfer")
                .content(objectMapper.writeValueAsString(withdrawDTO))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pin").exists());
    }

    @SneakyThrows
    @Test
    void transferAmountBadRequest() {
        TransferDTO withdrawDTO = new TransferDTO(-1, -2, -1, 12345);
        mockMvc.perform(post("/api/balance/transfer")
                .content(objectMapper.writeValueAsString(withdrawDTO))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.pin").doesNotExist());
    }
}