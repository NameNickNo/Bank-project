package com.project.account.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.project.account.dto.AccountCreateDTO;
import com.project.account.dto.ProfileDTO;
import com.project.account.model.Account;
import com.project.account.model.BalanceOperation;
import com.project.account.service.AccountService;
import com.project.account.service.MessageProducer;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AccountController.class)
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @Autowired
    private ModelMapper modelMapper;

    @MockBean
    private MessageProducer messageProducer;

    @Autowired
    private ObjectMapper objectMapper;

    @TestConfiguration
    static class TestConfig {

        @Bean
        public ModelMapper modelMapper() {
            return new ModelMapper();
        }

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
    void findAllShouldWork() {
        List<Account> accounts = List.of(
                new Account(1, "Name", 1234, 100.0),
                new Account(2, "Name2", 1234, 200.0)
        );
        when(accountService.findAll()).thenReturn(accounts);

        mockMvc.perform(get("/api/account").contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(accounts.size()))
                .andExpect(jsonPath("$[0].number").value(1))
                .andExpect(jsonPath("$[1].username").value("Name2"));
    }

    @SneakyThrows
    @Test
    void getOperationsShouldWork() {
        long number = 1;
        Account account = new Account(number, "Name", 1234, 100.0);
        account.setBalanceOperations(List.of(new BalanceOperation(account, 50)));

        when(accountService.findByNumberWithOperation(number)).thenReturn(Optional.of(account));

        mockMvc.perform(get("/api/account/{number}", number).contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.number").value(1))
                .andExpect(jsonPath("$.balanceOperations[0].amount").value(50));
    }

    @SneakyThrows
    @Test
    void getOperationsNotFound() {
        long number = 3;

        mockMvc.perform(get("/api/account/{number}", number).contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @SneakyThrows
    @Test
    public void createAccountShouldWork() {
        AccountCreateDTO createDTO = new AccountCreateDTO("name", 1234,
                new ProfileDTO("I'm", "test", LocalDate.now()));

        mockMvc.perform(post("/api/account/create")
                .content(objectMapper.writeValueAsString(createDTO))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").exists());
    }

    @SneakyThrows
    @Test
    public void createAccountShouldNotWork() {
        AccountCreateDTO createDTO = new AccountCreateDTO("name", 12341,
                new ProfileDTO("I'm", "test", LocalDate.now()));

        mockMvc.perform(post("/api/account/create")
                .content(objectMapper.writeValueAsString(createDTO))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.username").doesNotExist());
    }
}