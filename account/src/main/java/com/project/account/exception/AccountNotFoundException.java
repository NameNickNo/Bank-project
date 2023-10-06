package com.project.account.exception;

public class AccountNotFoundException extends RuntimeException {
    public AccountNotFoundException(long accountNumber) {
        super("Account with number " + accountNumber + " not found!");
    }
}
