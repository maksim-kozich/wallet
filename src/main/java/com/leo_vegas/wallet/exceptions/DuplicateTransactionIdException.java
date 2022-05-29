package com.leo_vegas.wallet.exceptions;

public class DuplicateTransactionIdException extends RuntimeException {
    public DuplicateTransactionIdException(String transactionId) {
        super("Duplicate transaction id '" + transactionId + "'");
    }
}
