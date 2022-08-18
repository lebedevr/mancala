package com.lebedevrs9.mancala.controller;

import com.lebedevrs9.mancala.model.Game;
import com.lebedevrs9.mancala.service.GameService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/games")
    public List<Game> getWaitingGames() {
        return gameService.getWaitingGames();
    }

    @GetMapping("/games/{id}")
    public Game getGame(@PathVariable String id) {
        return gameService.getGame(id);
    }

    @PostMapping("/games")
    public Game addGame(@RequestBody Game game) {
        return gameService.addGame(game);
    }

    @PostMapping("/games/join/{id}")
    public Game join(@RequestBody String playerId, @PathVariable String id) {
        return gameService.join(id, playerId);
    }

}
