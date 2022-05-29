package com.leo_vegas.wallet.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.leo_vegas.wallet.model.Balance;
import com.leo_vegas.wallet.model.Player;

public interface BalanceRepository extends JpaRepository<Balance, Long> {
    List<Balance> findByPlayer(Player player);
}
