package com.leo_vegas.wallet.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TransactionDto {
    private String transactionId;

    private TransactionTypeDto type;

    private double amount;

    @JsonProperty("transaction_id")
    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public TransactionTypeDto getType() {
        return type;
    }

    public void setType(TransactionTypeDto type) {
        this.type = type;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
