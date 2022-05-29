package com.leo_vegas.wallet.exceptions;

public class PlayerNotFoundException extends RuntimeException {
    public PlayerNotFoundException(Long id) {
        super("Could not find player " + id);
    }
}
