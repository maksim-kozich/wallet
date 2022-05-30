package com.leo_vegas.wallet.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.leo_vegas.wallet.exceptions.DuplicateTransactionIdException;
import com.leo_vegas.wallet.exceptions.InsufficientFundsException;
import com.leo_vegas.wallet.exceptions.PlayerNotFoundException;
import com.leo_vegas.wallet.model.Balance;
import com.leo_vegas.wallet.model.Player;
import com.leo_vegas.wallet.model.Transaction;
import com.leo_vegas.wallet.model.TransactionType;
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
    public void debitAmount(Long playerId, String transactionId, double amount) {
        try {
            this.debitAmountInner(playerId, transactionId, amount);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateTransactionIdException(transactionId);
        }
    }

    @Transactional
    public void debitAmountInner(Long playerId, String transactionId, double amount) {
        Player player = playerRepository.findById(playerId)
            .orElseThrow(() -> new PlayerNotFoundException(playerId));
        Balance balance = player.getBalance();
        double mew_amount = balance.getAmount() - amount;
        if (mew_amount >= 0) {
            Transaction transaction = new Transaction();
            transaction.setPlayer(player);
            transaction.setTransactionId(transactionId);
            transaction.setType(TransactionType.DEBIT);
            transaction.setAmount(amount);

            balance.setAmount(mew_amount);

            player.getTransactions().add(transaction);
            playerRepository.save(player);
        } else {
            throw new InsufficientFundsException();
        }
    }

    @Override
    public void creditAmount(Long playerId, String transactionId, double amount) {
        try {
            this.creditAmountInner(playerId, transactionId, amount);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateTransactionIdException(transactionId);
        }
    }

    @Transactional
    private void creditAmountInner(Long playerId, String transactionId, double amount) {
        Player player = playerRepository.findById(playerId)
            .orElseThrow(() -> new PlayerNotFoundException(playerId));
        Balance balance = player.getBalance();
        double mew_amount = balance.getAmount() + amount;

        Transaction transaction = new Transaction();
        transaction.setPlayer(player);
        transaction.setTransactionId(transactionId);
        transaction.setType(TransactionType.CREDIT);
        transaction.setAmount(amount);

        balance.setAmount(mew_amount);
        player.getTransactions().add(transaction);
        playerRepository.save(player);
    }

    @Override
    public List<Transaction> getPlayerTransactions(Long playerId) {
        return this.playerRepository.findById(playerId)
            .orElseThrow(() -> new PlayerNotFoundException(playerId))
            .getTransactions();
    }
}
