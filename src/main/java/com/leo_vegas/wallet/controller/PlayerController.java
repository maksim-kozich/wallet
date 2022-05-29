package com.leo_vegas.wallet.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.leo_vegas.wallet.dto.BalanceDto;
import com.leo_vegas.wallet.dto.CreditDto;
import com.leo_vegas.wallet.dto.DebitDto;
import com.leo_vegas.wallet.dto.TransactionDto;
import com.leo_vegas.wallet.dto.TransactionTypeDto;
import com.leo_vegas.wallet.exceptions.DuplicateTransactionIdException;
import com.leo_vegas.wallet.model.Balance;
import com.leo_vegas.wallet.model.Transaction;
import com.leo_vegas.wallet.service.IPLayerService;

@RestController
public class PlayerController {
    private final IPLayerService playerService;

    public PlayerController(IPLayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping("/players/{id}/balance")
    public BalanceDto balance(@PathVariable Long id) {
        Balance balance = this.playerService.getPlayerBalance(id);
        BalanceDto balanceDto = new BalanceDto();
        balanceDto.setAmount(balance.getAmount());
        return balanceDto;
    }

    @PostMapping("/players/{id}/debit")
    void debit(@PathVariable Long id, @RequestBody DebitDto debit) {
        try {
            this.playerService.debitAmount(id, debit.getTransactionId(), debit.getAmount());
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateTransactionIdException(debit.getTransactionId());
        }
    }

    @PostMapping("/players/{id}/credit")
    void credit(@PathVariable Long id, @RequestBody CreditDto credit) {
        try {
            this.playerService.creditAmount(id, credit.getTransactionId(), credit.getAmount());
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateTransactionIdException(credit.getTransactionId());
        }
    }

    @GetMapping("/players/{id}/transactions")
    public List<TransactionDto> transactions(@PathVariable Long id) {
        List<Transaction> transactions = this.playerService.getPlayerTransactions(id);
        return transactions.stream().map(model -> {
            TransactionDto dto = new TransactionDto();
            dto.setTransactionId(model.getTransactionId());
            switch (model.getType()) {
                case DEBIT -> dto.setType(TransactionTypeDto.DEBIT);
                case CREDIT -> dto.setType(TransactionTypeDto.CREDIT);
            }
            dto.setAmount(model.getAmount());
            return dto;
        }).collect(Collectors.toList());
    }
}
