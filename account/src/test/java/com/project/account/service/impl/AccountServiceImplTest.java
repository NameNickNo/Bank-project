package com.project.account.service.impl;

import com.project.account.model.Account;
import com.project.account.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    private final Account testAccount = new Account(1, "Name", 1234, 100);
    private final List<Account> testAccounts = List.of(testAccount);

    @InjectMocks
    private AccountServiceImpl accountService;

    @Mock
    private AccountRepository accountRepository;

    @Test
    void findAllShouldWork() {
        when(accountRepository.findAll()).thenReturn(testAccounts);

        List<Account> accounts = accountService.findAll();
        assertEquals(testAccounts, accounts);
        verify(accountRepository, times(1)).findAll();
    }

    @Test
    void findByNumberShouldWork() {
        Optional<Account> testAccount = Optional.of(this.testAccount);
        long number = 1;
        when(accountRepository.findByNumber(number)).thenReturn(testAccount);

        Optional<Account> account = accountService.findByNumber(number);
        assertEquals(testAccount, account);
        verify(accountRepository, times(1)).findByNumber(number);
    }

    @Test
    void findByNumberWithOperationShouldWork() {
        Optional<Account> testAccount = Optional.of(this.testAccount);
        long number = 1;
        when(accountRepository.findAccountWithOperationsByNumber(number)).thenReturn(testAccount);

        Optional<Account> account = accountService.findByNumberWithOperation(number);
        assertEquals(testAccount, account);
        verify(accountRepository, times(1)).findAccountWithOperationsByNumber(number);
    }

    @Test
    void createAccountShouldWork() {
        accountService.createAccount(testAccount);
        verify(accountRepository, times(1)).save(testAccount);
    }

    @Test
    void changeBalanceShouldWork() {
        double newBalance = 200;
        accountService.changeBalance(testAccount, newBalance);

        assertEquals(newBalance, testAccount.getBalance());
        verify(accountRepository, times(1)).save(testAccount);

    }
}