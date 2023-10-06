package com.project.account.service.impl;

import com.project.account.dto.DepositDTO;
import com.project.account.dto.WithdrawDTO;
import com.project.account.model.Account;
import com.project.account.model.BalanceOperation;
import com.project.account.repository.BalanceOperationRepository;
import com.project.account.service.AccountService;
import com.project.account.exception.AccountNotFoundException;
import com.project.account.exception.BalanceInsufficientFundsException;
import com.project.account.exception.PinAccountVerificationException;
import com.project.account.util.Message;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BalanceOperationServiceImplTest {

    @InjectMocks
    private BalanceOperationServiceImpl balanceOperationService;

    @Mock
    private AccountService accountService;

    @Mock
    private BalanceOperationRepository operationRepository;

    private final Account account = new Account(1, "Ivan", 1111, 100);

    @Test
    void changeBalanceDepositMustWork() {
        DepositDTO depositDTO = new DepositDTO(1, 100, 1111);
        when(accountService.findByNumber(depositDTO.getAccountNumber())).thenReturn(Optional.of(account));

        balanceOperationService.createDepositOperation(depositDTO);

        double newBalance = account.getBalance() + depositDTO.getAmount();
        verify(accountService, times(1)).changeBalance(account, newBalance);
        verify(operationRepository, times(1)).save(new BalanceOperation(account, depositDTO.getAmount()));
    }

    @Test
    void changeBalanceDepositThrowPinVerificationException() {
        DepositDTO depositDTO = new DepositDTO(1, 100, 3333);
        when(accountService.findByNumber(depositDTO.getAccountNumber())).thenReturn(Optional.of(account));

        PinAccountVerificationException exception = assertThrows(PinAccountVerificationException.class,
                () -> balanceOperationService.createDepositOperation(depositDTO));

        assertEquals(Message.WRONG_PIN, exception.getMessage());
        verify(accountService, never()).changeBalance(account, 100);
        verify(operationRepository, never()).save(new BalanceOperation());
    }

    @Test
    void changeBalanceDepositThrowAccountNotFoundException() {
        DepositDTO depositDTO = new DepositDTO(0, 100, 3333);

        assertThrows(AccountNotFoundException.class, () -> balanceOperationService.createDepositOperation(depositDTO));

        verify(accountService, never()).changeBalance(account, 100);
        verify(operationRepository, never()).save(new BalanceOperation());
    }

    @Test
    void changeBalanceWithdrawMustWork() {
        WithdrawDTO withdrawDTO = new WithdrawDTO(1, 100, 1111);
        when(accountService.findByNumber(withdrawDTO.getAccountNumber())).thenReturn(Optional.of(account));

        balanceOperationService.createWithdrawOperation(withdrawDTO);

        double newBalance = account.getBalance() - withdrawDTO.getAmount();
        verify(accountService, times(1)).changeBalance(account, newBalance);
        verify(operationRepository, times(1)).save(new BalanceOperation(account, withdrawDTO.getAmount()));

    }

    @Test
    void changeBalanceWithdrawThrowPinVerificationException() {
        WithdrawDTO withdrawDTO = new WithdrawDTO(1, 100, 3333);
        when(accountService.findByNumber(withdrawDTO.getAccountNumber())).thenReturn(Optional.of(account));

        PinAccountVerificationException exception = assertThrows(PinAccountVerificationException.class,
                () -> balanceOperationService.createWithdrawOperation(withdrawDTO));

        assertEquals(Message.WRONG_PIN, exception.getMessage());
        verify(accountService, never()).changeBalance(account, 100);
        verify(operationRepository, never()).save(new BalanceOperation());
    }

    @Test
    void changeBalanceWithdrawThrowBalanceInsufficientFundsException() {
        WithdrawDTO withdrawDTO = new WithdrawDTO(1, 150, 1111);
        when(accountService.findByNumber(withdrawDTO.getAccountNumber())).thenReturn(Optional.of(account));

        BalanceInsufficientFundsException exception = assertThrows(BalanceInsufficientFundsException.class,
                () -> balanceOperationService.createWithdrawOperation(withdrawDTO));

        assertEquals(Message.INSUFFICIENT_FUNDS, exception.getMessage());
        verify(accountService, never()).changeBalance(account, 100);
        verify(operationRepository, never()).save(new BalanceOperation());
    }

    @Test
    void changeBalanceWithdrawThrowAccountNotFoundException() {
        WithdrawDTO withdrawDTO = new WithdrawDTO(0, 150, 1111);

        assertThrows(AccountNotFoundException.class, () -> balanceOperationService.createWithdrawOperation(withdrawDTO));

        verify(accountService, never()).changeBalance(account, 100);
        verify(operationRepository, never()).save(new BalanceOperation());

    }
}