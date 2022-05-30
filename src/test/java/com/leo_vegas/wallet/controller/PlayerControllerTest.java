package com.leo_vegas.wallet.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.leo_vegas.wallet.dto.BalanceDto;
import com.leo_vegas.wallet.dto.TransactionDto;
import com.leo_vegas.wallet.dto.TransactionTypeDto;
import com.leo_vegas.wallet.exceptions.DuplicateTransactionIdException;
import com.leo_vegas.wallet.exceptions.InsufficientFundsException;
import com.leo_vegas.wallet.exceptions.PlayerNotFoundException;
import com.leo_vegas.wallet.model.Balance;
import com.leo_vegas.wallet.model.Transaction;
import com.leo_vegas.wallet.service.IPLayerService;

@WebMvcTest(PlayerController.class)
public class PlayerControllerTest {

    public static final long PLAYER_ID = 1L;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IPLayerService playerServiceMock;

    @MockBean
    private ModelMapper modelMapperMock;

    @Test
    public void balanceOk200() throws Exception {
        Balance balance = new Balance();
        BalanceDto balanceDto = new BalanceDto();
        double amount = 2.0;
        balanceDto.setAmount(amount);
        when(playerServiceMock.getPlayerBalance(PLAYER_ID)).thenReturn(balance);
        when(modelMapperMock.map(balance, BalanceDto.class)).thenReturn(balanceDto);
        this.mockMvc.perform(get("/players/" + PLAYER_ID + "/balance"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().string(equalTo("{\"amount\":" + amount + "}")));
    }

    @Test
    public void balancePlayerNotFound404() throws Exception {
        when(playerServiceMock.getPlayerBalance(PLAYER_ID)).thenThrow(new PlayerNotFoundException(PLAYER_ID));
        this.mockMvc.perform(get("/players/" + PLAYER_ID + "/balance"))
            .andDo(print())
            .andExpect(status().isNotFound());
    }

    @Test
    public void debitOk200() throws Exception {
        String transactionId = "test";
        double amount = 2.0;
        doNothing().when(playerServiceMock).debitAmount(PLAYER_ID, transactionId, amount);
        this.mockMvc.perform(post("/players/" + PLAYER_ID + "/debit")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"transaction_id\": \"" + transactionId + "\"," + "\"amount\":" + amount + "}")
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().string(equalTo("")));
    }

    @Test
    public void debitPlayerNotFound404() throws Exception {
        String transactionId = "test";
        double amount = 2.0;
        doThrow(new PlayerNotFoundException(PLAYER_ID)).when(playerServiceMock).debitAmount(PLAYER_ID, transactionId, amount);
        this.mockMvc.perform(
                post("/players/" + PLAYER_ID + "/debit")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"transaction_id\": \"" + transactionId + "\"," + "\"amount\":" + amount + "}")
            )
            .andDo(print())
            .andExpect(status().isNotFound());
    }

    @Test
    public void debitInsufficientFunds422() throws Exception {
        String transactionId = "test";
        double amount = 2.0;
        doThrow(new InsufficientFundsException()).when(playerServiceMock).debitAmount(PLAYER_ID, transactionId, amount);
        this.mockMvc.perform(
                post("/players/" + PLAYER_ID + "/debit")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"transaction_id\": \"" + transactionId + "\"," + "\"amount\":" + amount + "}")
            )
            .andDo(print())
            .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void debitDuplicateTransactionId400() throws Exception {
        String transactionId = "test";
        double amount = 2.0;
        doThrow(new DuplicateTransactionIdException(transactionId)).when(playerServiceMock).debitAmount(PLAYER_ID, transactionId, amount);
        this.mockMvc.perform(
                post("/players/" + PLAYER_ID + "/debit")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"transaction_id\": \"" + transactionId + "\"," + "\"amount\":" + amount + "}")
            )
            .andDo(print())
            .andExpect(status().isBadRequest());
    }

    @Test
    public void debitEmptyTransactionId400() throws Exception {
        String transactionId = "";
        double amount = 2.0;
        this.mockMvc.perform(
                post("/players/" + PLAYER_ID + "/debit")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"transaction_id\": \"" + transactionId + "\"," + "\"amount\":" + amount + "}")
            )
            .andDo(print())
            .andExpect(status().isBadRequest());
    }

    @Test
    public void debitNegativeAmount400() throws Exception {
        String transactionId = "test";
        double amount = -2.0;
        this.mockMvc.perform(
                post("/players/" + PLAYER_ID + "/debit")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"transaction_id\": \"" + transactionId + "\"," + "\"amount\":" + amount + "}")
            )
            .andDo(print())
            .andExpect(status().isBadRequest());
    }

    @Test
    public void creditOk200() throws Exception {
        String transactionId = "test";
        double amount = 2.0;
        doNothing().when(playerServiceMock).creditAmount(PLAYER_ID, transactionId, amount);
        this.mockMvc.perform(post("/players/" + PLAYER_ID + "/credit")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"transaction_id\": \"" + transactionId + "\"," + "\"amount\":" + amount + "}")
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().string(equalTo("")));
    }

    @Test
    public void creditPlayerNotFound404() throws Exception {
        String transactionId = "test";
        double amount = 2.0;
        doThrow(new PlayerNotFoundException(PLAYER_ID)).when(playerServiceMock).creditAmount(PLAYER_ID, transactionId, amount);
        this.mockMvc.perform(
                post("/players/" + PLAYER_ID + "/credit")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"transaction_id\": \"" + transactionId + "\"," + "\"amount\":" + amount + "}")
            )
            .andDo(print())
            .andExpect(status().isNotFound());
    }

    @Test
    public void creditInsufficientFunds422() throws Exception {
        String transactionId = "test";
        double amount = 2.0;
        doThrow(new InsufficientFundsException()).when(playerServiceMock).creditAmount(PLAYER_ID, transactionId, amount);
        this.mockMvc.perform(
                post("/players/" + PLAYER_ID + "/credit")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"transaction_id\": \"" + transactionId + "\"," + "\"amount\":" + amount + "}")
            )
            .andDo(print())
            .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void creditDuplicateTransactionId400() throws Exception {
        String transactionId = "test";
        double amount = 2.0;
        doThrow(new DuplicateTransactionIdException(transactionId)).when(playerServiceMock).creditAmount(PLAYER_ID, transactionId, amount);
        this.mockMvc.perform(
                post("/players/" + PLAYER_ID + "/credit")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"transaction_id\": \"" + transactionId + "\"," + "\"amount\":" + amount + "}")
            )
            .andDo(print())
            .andExpect(status().isBadRequest());
    }

    @Test
    public void creditEmptyTransactionId400() throws Exception {
        String transactionId = "";
        double amount = 2.0;
        this.mockMvc.perform(
                post("/players/" + PLAYER_ID + "/credit")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"transaction_id\": \"" + transactionId + "\"," + "\"amount\":" + amount + "}")
            )
            .andDo(print())
            .andExpect(status().isBadRequest());
    }

    @Test
    public void creditNegativeAmount400() throws Exception {
        String transactionId = "test";
        double amount = -2.0;
        this.mockMvc.perform(
                post("/players/" + PLAYER_ID + "/credit")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"transaction_id\": \"" + transactionId + "\"," + "\"amount\":" + amount + "}")
            )
            .andDo(print())
            .andExpect(status().isBadRequest());
    }

    @Test
    public void transactionsOk200() throws Exception {
        Transaction transaction = new Transaction();
        TransactionDto transactionDto = new TransactionDto();
        String transactionId = "test";
        double amount = 2.0;
        transactionDto.setAmount(amount);
        transactionDto.setTransactionId(transactionId);
        transactionDto.setType(TransactionTypeDto.DEBIT);
        when(playerServiceMock.getPlayerTransactions(PLAYER_ID)).thenReturn(List.of(transaction));
        when(modelMapperMock.map(transaction, TransactionDto.class)).thenReturn(transactionDto);
        this.mockMvc.perform(get("/players/" + PLAYER_ID + "/transactions"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().string(equalTo("[{\"type\":\"debit\",\"amount\":" + amount + ",\"transaction_id\":\"" + transactionId + "\"}]")));
    }

    @Test
    public void transactionsPlayerNotFound404() throws Exception {
        when(playerServiceMock.getPlayerTransactions(PLAYER_ID)).thenThrow(new PlayerNotFoundException(PLAYER_ID));
        this.mockMvc.perform(get("/players/" + PLAYER_ID + "/transactions"))
            .andDo(print())
            .andExpect(status().isNotFound());
    }
}
