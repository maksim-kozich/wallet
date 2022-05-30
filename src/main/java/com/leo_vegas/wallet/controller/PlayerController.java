package com.leo_vegas.wallet.controller;

import java.util.List;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.leo_vegas.wallet.configuration.MapperUtil;
import com.leo_vegas.wallet.dto.BalanceDto;
import com.leo_vegas.wallet.dto.CreditDto;
import com.leo_vegas.wallet.dto.DebitDto;
import com.leo_vegas.wallet.dto.TransactionDto;
import com.leo_vegas.wallet.model.Balance;
import com.leo_vegas.wallet.model.Transaction;
import com.leo_vegas.wallet.service.IPLayerService;

@RestController
public class PlayerController {
    private final IPLayerService playerService;
    private final ModelMapper modelMapper;

    public PlayerController(IPLayerService playerService, ModelMapper modelMapper) {
        this.playerService = playerService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/players/{playerId}/balance")
    public BalanceDto balance(@PathVariable Long playerId) {
        Balance balance = this.playerService.getPlayerBalance(playerId);
        return modelMapper.map(balance, BalanceDto.class);
    }

    @PostMapping("/players/{playerId}/debit")
    void debit(@PathVariable Long playerId, @Valid @RequestBody DebitDto debit) {
        this.playerService.debitAmount(playerId, debit.getTransactionId(), debit.getAmount());
    }

    @PostMapping("/players/{playerId}/credit")
    void credit(@PathVariable Long playerId, @Valid @RequestBody CreditDto credit) {
        this.playerService.creditAmount(playerId, credit.getTransactionId(), credit.getAmount());
    }

    @GetMapping("/players/{playerId}/transactions")
    public List<TransactionDto> transactions(@PathVariable Long playerId) {
        List<Transaction> transactions = this.playerService.getPlayerTransactions(playerId);
        return MapperUtil.convertList(transactions, this::convertToTransactionDto);
    }

    private TransactionDto convertToTransactionDto(Transaction transaction) {
        return modelMapper.map(transaction, TransactionDto.class);
    }
}
