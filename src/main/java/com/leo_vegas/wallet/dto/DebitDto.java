package com.leo_vegas.wallet.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DebitDto {
    private String transactionId;

    private double amount;

    @JsonProperty("transaction_id")
    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
