package com.leo_vegas.wallet.exceptions;

public class InsufficientFundsException extends RuntimeException {
    public InsufficientFundsException() {
        super("Player has insufficient funds");
    }
}
