package com.leo_vegas.wallet;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.leo_vegas.wallet.model.Balance;
import com.leo_vegas.wallet.model.Player;
import com.leo_vegas.wallet.model.Transaction;
import com.leo_vegas.wallet.model.TransactionType;
import com.leo_vegas.wallet.repository.BalanceRepository;
import com.leo_vegas.wallet.repository.PlayerRepository;
import com.leo_vegas.wallet.repository.TransactionRepository;

@SpringBootApplication
public class WalletApplication {

    public static void main(String[] args) {
        SpringApplication.run(WalletApplication.class, args);
    }

    @Bean
    public CommandLineRunner mappingDemo(PlayerRepository playerRepository, BalanceRepository balanceRepository, TransactionRepository transactionRepository) {
        return args -> {
            // Clean up database tables
            playerRepository.deleteAllInBatch();
            balanceRepository.deleteAllInBatch();
            transactionRepository.deleteAllInBatch();

            // create a new book
            Player player1 = new Player("FirstName 1", "LastName1");
            Balance balance1 = new Balance();
            balance1.setPlayer(player1);
            player1.setBalance(balance1);

            Player player2 = new Player("FirstName 2", "LastName2");
            Balance balance2 = new Balance();
            double amount = 2.0;
            balance2.setAmount(amount);
            balance2.setPlayer(player2);
            player2.setBalance(balance2);
            Transaction transaction = new Transaction();
            transaction.setType(TransactionType.CREDIT);
            transaction.setTransactionId("initial");
            transaction.setPlayer(player2);
            transaction.setAmount(amount);
            player2.setTransactions(List.of(transaction));

            // save the book
            playerRepository.save(player1);
            playerRepository.save(player2);
        };
    }
}
