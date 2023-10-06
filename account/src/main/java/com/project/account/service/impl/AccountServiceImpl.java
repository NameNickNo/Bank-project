package com.project.account.service.impl;

import com.project.account.model.Account;
import com.project.account.repository.AccountRepository;
import com.project.account.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Primary
@Transactional
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Transactional(readOnly = true)
    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Account> findByNumber(long number) {
        return accountRepository.findByNumber(number);
    }

    @Transactional(readOnly = true)
    public Optional<Account> findByNumberWithOperation(long number) {
        return accountRepository.findAccountWithOperationsByNumber(number);
    }

    public void createAccount(Account account) {
        accountRepository.save(account);
    }

    public void changeBalance(Account account, double newBalance) {
        account.setBalance(newBalance);
        accountRepository.save(account);
    }
}
