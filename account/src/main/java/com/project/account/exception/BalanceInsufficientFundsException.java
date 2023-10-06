package com.project.account.exception;

public class BalanceInsufficientFundsException extends RuntimeException{
    public BalanceInsufficientFundsException(String message) {
        super(message);
    }
}
