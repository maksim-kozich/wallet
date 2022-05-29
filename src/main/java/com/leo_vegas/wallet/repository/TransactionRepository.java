package com.leo_vegas.wallet.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.leo_vegas.wallet.model.Player;
import com.leo_vegas.wallet.model.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByPlayer(Player player);
}
