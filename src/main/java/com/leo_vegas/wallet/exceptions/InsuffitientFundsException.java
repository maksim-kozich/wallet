package com.leo_vegas.wallet.exceptions;

public class InsuffitientFundsException extends RuntimeException {
    public InsuffitientFundsException() {
        super("Player has insufficient funds");
    }
}
