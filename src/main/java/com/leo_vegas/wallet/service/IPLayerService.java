package com.leo_vegas.wallet.service;

import java.util.List;

import com.leo_vegas.wallet.model.Balance;
import com.leo_vegas.wallet.model.Transaction;

public interface IPLayerService {
    Balance getPlayerBalance(Long playerId);

    void debitAmount(Long playerId, double amount);

    void creditAmount(Long playerOd, double amount);

    List<Transaction> getPlayerTransactions(Long playerId);
}
