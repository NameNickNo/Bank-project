package com.project.account.service.impl;

import com.project.account.dto.TransferDTO;
import com.project.account.exception.AccountNotFoundException;
import com.project.account.exception.BadRequestException;
import com.project.account.exception.BalanceInsufficientFundsException;
import com.project.account.exception.PinAccountVerificationException;
import com.project.account.model.Account;
import com.project.account.model.Transfer;
import com.project.account.repository.TransferOperationRepository;
import com.project.account.service.AccountService;
import com.project.account.util.Message;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransferOperationServiceImplTest {

    @InjectMocks
    private TransferOperationServiceImpl transferOperationService;

    @Mock
    private AccountService accountService;

    @Mock
    private TransferOperationRepository transferOperationRepository;

    private final Account account = new Account(1, "Ivan", 1111, 100);
    private final Account accountSecond = new Account(2, "Igor", 2222, 100);

    @Test
    void transferMustWork() {
        TransferDTO transferDTO = new TransferDTO(1, 2, 100, 1111);
        when(accountService.findByNumber(transferDTO.getAccountNumberFrom())).thenReturn(Optional.of(account));
        when(accountService.findByNumber(transferDTO.getAccountNumberTo())).thenReturn(Optional.of(accountSecond));
        double sumBalanceBeforeOperation = account.getBalance() + accountSecond.getBalance();

        transferOperationService.createTransfer(transferDTO);

        double newBalance = account.getBalance() - transferDTO.getAmount();
        double newBalanceSecond = accountSecond.getBalance() + transferDTO.getAmount();
        assertEquals(sumBalanceBeforeOperation, newBalance + newBalanceSecond);

        verify(accountService, times(1)).changeBalance(account, newBalance);
        verify(accountService, times(1)).changeBalance(accountSecond, newBalanceSecond);
        verify(transferOperationRepository, times(1)).save(new Transfer(account, accountSecond, transferDTO.getAmount()));

    }

    @Test
    void transferThrowPinVerificationException() {
        TransferDTO transferDTO = new TransferDTO(1, 2, 100, 2222);
        when(accountService.findByNumber(transferDTO.getAccountNumberFrom())).thenReturn(Optional.of(account));

        PinAccountVerificationException exception = assertThrows(PinAccountVerificationException.class,
                () -> transferOperationService.createTransfer(transferDTO));

        assertEquals(Message.WRONG_PIN, exception.getMessage());
        verify(accountService, never()).changeBalance(account, 100);
        verify(accountService, never()).changeBalance(account, 100);
        verify(transferOperationRepository, never()).save(new Transfer());
    }

    @Test
    void transferThrowBalanceInsufficientFundsException() {
        TransferDTO transferDTO = new TransferDTO(1, 2, 150, 1111);
        when(accountService.findByNumber(transferDTO.getAccountNumberFrom())).thenReturn(Optional.of(account));
        when(accountService.findByNumber(transferDTO.getAccountNumberTo())).thenReturn(Optional.of(accountSecond));

        BalanceInsufficientFundsException exception = assertThrows(BalanceInsufficientFundsException.class,
                () -> transferOperationService.createTransfer(transferDTO));

        assertEquals(Message.INSUFFICIENT_FUNDS, exception.getMessage());
        verify(accountService, never()).changeBalance(account, 100);
        verify(accountService, never()).changeBalance(account, 100);
        verify(transferOperationRepository, never()).save(new Transfer());
    }

    @Test
    void transferThrowAccountNotFoundException() {
        TransferDTO transferDTO = new TransferDTO(0, 2, 150, 1111);

        assertThrows(AccountNotFoundException.class, () -> transferOperationService.createTransfer(transferDTO));

        verify(accountService, never()).changeBalance(account, 100);
        verify(accountService, never()).changeBalance(account, 100);
        verify(transferOperationRepository, never()).save(new Transfer());

    }

    @Test
    void transferThrowBadRequestException() {
        TransferDTO transferDTO = new TransferDTO(1, 1, 150, 1111);
        when(accountService.findByNumber(transferDTO.getAccountNumberFrom())).thenReturn(Optional.of(account));
        when(accountService.findByNumber(transferDTO.getAccountNumberTo())).thenReturn(Optional.of(account));

        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> transferOperationService.createTransfer(transferDTO));

        assertEquals(Message.IDENTICAL_ACCOUNT, exception.getMessage());
        verify(accountService, never()).changeBalance(account, 100);
        verify(accountService, never()).changeBalance(account, 100);
        verify(transferOperationRepository, never()).save(new Transfer());

    }
}