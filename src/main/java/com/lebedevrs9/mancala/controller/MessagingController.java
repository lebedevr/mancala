package com.lebedevrs9.mancala.controller;

import com.lebedevrs9.mancala.model.Game;
import com.lebedevrs9.mancala.service.GameService;
import com.lebedevrs9.mancala.model.Move;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class MessagingController {

    private final GameService gameService;

    public MessagingController(GameService gameService) {
        this.gameService = gameService;
    }

    @MessageMapping("/move/{id}")
    @SendTo("/mancala/state/{id}")
    public Game move(@DestinationVariable Integer id, Move move) {
        Game game = gameService.getGame(id);
        game.move(move.getPlayer(), move.getIndex());
        return game;
    }

    @MessageMapping("/join/{id}")
    @SendTo("/mancala/state/{id}")
    public Game join(@DestinationVariable Integer id) {
        return gameService.getGame(id);
    }
}
