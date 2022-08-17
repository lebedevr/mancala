package com.lebedevrs9.mancala.service;

import com.lebedevrs9.mancala.model.Game;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class GameService {
    private final Map<Integer, Game> games = new ConcurrentHashMap<>();

    private final AtomicInteger idCounter = new AtomicInteger(0);

    public List<Game> getWaitingGames() {
        return games.values()
                .stream()
                .filter(g -> g.getStatus() == Game.Status.WAITING)
                .collect(Collectors.toList());
    }

    public Game getGame(Integer id) {
        return games.get(id);
    }

    public Game addGame(@RequestBody Game game) {
        int id = idCounter.incrementAndGet();
        game.setId(id);
        games.put(id, game);
        return game;
    }

    public Game join(Integer id, String playerId) {
        Game game = games.get(id);
        if (!playerId.equals(game.getFirstPlayer())) {
            game.setSecondPlayer(playerId);
            game.setStatus(Game.Status.STARTED);
        }
        return game;
    }
}
