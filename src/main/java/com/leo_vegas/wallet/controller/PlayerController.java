package com.leo_vegas.wallet.controller;

import java.util.List;

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

    @GetMapping("/players/{id}/balance")
    public BalanceDto balance(@PathVariable Long id) {
        Balance balance = this.playerService.getPlayerBalance(id);
        return modelMapper.map(balance, BalanceDto.class);
    }

    @PostMapping("/players/{id}/debit")
    void debit(@PathVariable Long id, @RequestBody DebitDto debit) {
        this.playerService.debitAmount(id, debit.getTransactionId(), debit.getAmount());
    }

    @PostMapping("/players/{id}/credit")
    void credit(@PathVariable Long id, @RequestBody CreditDto credit) {
        this.playerService.creditAmount(id, credit.getTransactionId(), credit.getAmount());
    }

    @GetMapping("/players/{id}/transactions")
    public List<TransactionDto> transactions(@PathVariable Long id) {
        List<Transaction> transactions = this.playerService.getPlayerTransactions(id);
        return MapperUtil.convertList(transactions, this::convertToTransactionDto);
    }

    private TransactionDto convertToTransactionDto(Transaction transaction) {
        return modelMapper.map(transaction, TransactionDto.class);
    }
}
