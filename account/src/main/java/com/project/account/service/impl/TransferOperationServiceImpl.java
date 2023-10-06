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
import com.project.account.service.TransferOperationService;
import com.project.account.util.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Primary
@Transactional
public class TransferOperationServiceImpl implements TransferOperationService {

    private final TransferOperationRepository transferOperationRepository;
    private final AccountService accountService;

    @Autowired
    public TransferOperationServiceImpl(TransferOperationRepository transferOperationRepository, AccountService accountService) {
        this.transferOperationRepository = transferOperationRepository;
        this.accountService = accountService;
    }

    @Override
    public void createTransfer(TransferDTO transferDTO) {
        Account accountFrom = accountService.findByNumber(transferDTO.getAccountNumberFrom())
                .orElseThrow(() -> new AccountNotFoundException(transferDTO.getAccountNumberFrom()));

        if (accountFrom.getPin() != transferDTO.getPin())
            throw new PinAccountVerificationException(Message.WRONG_PIN);
        
        Account accountTo = accountService.findByNumber(transferDTO.getAccountNumberTo())
                .orElseThrow(() -> new AccountNotFoundException(transferDTO.getAccountNumberTo()));
        
        if (accountFrom.getNumber() == accountTo.getNumber())
            throw new BadRequestException(Message.IDENTICAL_ACCOUNT);

        double transferAmount = transferDTO.getAmount();
        if (accountFrom.getBalance() - transferAmount < 0)
            throw new BalanceInsufficientFundsException(Message.INSUFFICIENT_FUNDS);

        accountService.changeBalance(accountFrom, accountFrom.getBalance() - transferAmount);
        accountService.changeBalance(accountTo, accountTo.getBalance() + transferAmount);

        transferOperationRepository.save(new Transfer(accountFrom, accountTo, transferAmount));
    }
}
