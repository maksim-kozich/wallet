package com.leo_vegas.wallet.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.leo_vegas.wallet.exceptions.PlayerNotFoundException;
import com.leo_vegas.wallet.model.Balance;
import com.leo_vegas.wallet.model.Transaction;
import com.leo_vegas.wallet.repository.PlayerRepository;

@Service
public class PLayerService implements IPLayerService {
    private final PlayerRepository playerRepository;

    public PLayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @Override
    public Balance getPlayerBalance(Long playerId) {
        return this.playerRepository.findById(playerId)
            .orElseThrow(() -> new PlayerNotFoundException(playerId))
            .getBalance();
    }

    @Override
    public void debitAmount(Long playerId, double amount) {
    }

    @Override
    public void creditAmount(Long playerOd, double amount) {
    }

    @Override
    public List<Transaction> getPlayerTransactions(Long playerId) {
        return this.playerRepository.findById(playerId)
            .orElseThrow(() -> new PlayerNotFoundException(playerId))
            .getTransactions();
    }
}
