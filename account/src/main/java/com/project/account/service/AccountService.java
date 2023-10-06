package com.project.account.service;

import com.project.account.model.Account;

import java.util.List;
import java.util.Optional;

public interface AccountService {

    List<Account> findAll();

    Optional<Account> findByNumber(long number);

    Optional<Account> findByNumberWithOperation(long number);

    void createAccount(Account account);

    void changeBalance(Account account, double newBalance);
}
