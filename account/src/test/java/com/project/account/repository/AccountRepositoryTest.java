package com.project.account.repository;

import com.project.account.exception.AccountNotFoundException;
import com.project.account.model.Account;
import com.project.account.model.Profile;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
class AccountRepositoryTest {

    @Autowired
    AccountRepository accountRepository;

    @Test
    void findByNumberShouldWork() {
        Optional<Account> account = accountRepository.findByNumber(3);
        assertEquals("user", account.get().getUsername());
    }

    @Test
    void findAccountWithOperationsByNumber() {
        Optional<Account> accountOptional = accountRepository.findAccountWithOperationsByNumber(3);
        Account account = accountOptional.orElseThrow(() -> new AccountNotFoundException(3));
        assertEquals("user", account.getUsername());
        assertNotEquals(Collections.emptyList(), account.getBalanceOperations());
    }
}