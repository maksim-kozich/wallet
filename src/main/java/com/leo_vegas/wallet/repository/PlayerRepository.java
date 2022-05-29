package com.leo_vegas.wallet.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.leo_vegas.wallet.model.Player;

public interface PlayerRepository extends JpaRepository<Player, Long> {
}
