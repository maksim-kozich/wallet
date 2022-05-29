package com.leo_vegas.wallet.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum TransactionTypeDto {
    @JsonProperty("debit")
    DEBIT,
    @JsonProperty("credit")
    CREDIT
}
