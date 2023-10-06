package com.project.account.service.impl;

import com.project.account.dto.DepositDTO;
import com.project.account.dto.WithdrawDTO;
import com.project.account.exception.AccountNotFoundException;
import com.project.account.exception.BalanceInsufficientFundsException;
import com.project.account.exception.PinAccountVerificationException;
import com.project.account.model.Account;
import com.project.account.model.BalanceOperation;
import com.project.account.repository.BalanceOperationRepository;
import com.project.account.service.AccountService;
import com.project.account.service.BalanceOperationService;
import com.project.account.util.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Primary
@Transactional
public class BalanceOperationServiceImpl implements BalanceOperationService {

    private final BalanceOperationRepository operationRepository;
    private final AccountService accountService;

    @Autowired
    public BalanceOperationServiceImpl(BalanceOperationRepository operationRepository, AccountService accountService) {
        this.operationRepository = operationRepository;
        this.accountService = accountService;
    }

    @Override
    public void createDepositOperation(DepositDTO depositDTO) {
        Account account = accountService.findByNumber(depositDTO.getAccountNumber())
                .orElseThrow(() -> new AccountNotFoundException(depositDTO.getAccountNumber()));

        if (account.getPin() != depositDTO.getPin())
            throw new PinAccountVerificationException(Message.WRONG_PIN);

        double depositAmount = depositDTO.getAmount();
        accountService.changeBalance(account, account.getBalance() + depositAmount);
        operationRepository.save(new BalanceOperation(account, depositAmount));
    }

    @Override
    public void createWithdrawOperation(WithdrawDTO withdrawDTO) {
        Account account = accountService.findByNumber(withdrawDTO.getAccountNumber())
                .orElseThrow(() -> new AccountNotFoundException(withdrawDTO.getAccountNumber()));

        if (account.getPin() != withdrawDTO.getPin())
            throw new PinAccountVerificationException(Message.WRONG_PIN);

        double withdrawAmount = withdrawDTO.getAmount();
        double newBalance = account.getBalance() - withdrawAmount;
        if (newBalance < 0)
            throw new BalanceInsufficientFundsException(Message.INSUFFICIENT_FUNDS);

        accountService.changeBalance(account, newBalance);
        operationRepository.save(new BalanceOperation(account, withdrawAmount));
    }
}
