package com.leo_vegas.wallet.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.leo_vegas.wallet.exceptions.PlayerNotFoundException;
import com.leo_vegas.wallet.model.Player;
import com.leo_vegas.wallet.repository.PlayerRepository;

@RestController
public class PlayerController {
    private final PlayerRepository repository;

    public PlayerController(PlayerRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/players")
    public List<Player> all() {
        return this.repository.findAll();
    }

    @PostMapping("/players")
    public Player newPlayer(@RequestBody Player player) {
        return this.repository.save(player);
    }

    @GetMapping("/players/{id}")
    public Player single(@PathVariable Long id) {
        return this.repository.findById(id).orElseThrow(() -> new PlayerNotFoundException(id));
    }
}
