package com.leo_vegas.wallet.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.leo_vegas.wallet.exceptions.DuplicateTransactionIdException;
import com.leo_vegas.wallet.exceptions.InsuffitientFundsException;
import com.leo_vegas.wallet.exceptions.PlayerNotFoundException;

@ControllerAdvice
public class PlayerNotFoundAdvice {
    @ResponseBody
    @ExceptionHandler(PlayerNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String playerNotFoundHandler(PlayerNotFoundException ex) {
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(InsuffitientFundsException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    String insufficientFundsHandler(InsuffitientFundsException ex) {
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(DuplicateTransactionIdException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String duplicateTransactionIdHandler(DuplicateTransactionIdException ex) {
        return ex.getMessage();
    }
}
