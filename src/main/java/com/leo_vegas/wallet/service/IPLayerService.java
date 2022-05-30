package com.leo_vegas.wallet.service;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;

import org.springframework.validation.annotation.Validated;

import com.leo_vegas.wallet.model.Balance;
import com.leo_vegas.wallet.model.Transaction;

@Validated
public interface IPLayerService {
    Balance getPlayerBalance(Long playerId);

    void debitAmount(Long playerId, @NotEmpty String transactionId, @Positive double amount);

    void creditAmount(Long playerOd, @NotEmpty String transactionId, @Positive double amount);

    List<Transaction> getPlayerTransactions(Long playerId);
}
