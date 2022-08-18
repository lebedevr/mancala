package com.lebedevrs9.mancala.service;

import com.lebedevrs9.mancala.model.Game;
import com.lebedevrs9.mancala.model.Move;
import com.lebedevrs9.mancala.repository.GameRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class GameService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final GameRepository gameRepository;
    public GameService(
            RedisTemplate<String, Object> redisTemplate,
            GameRepository gameRepository
    ) {
        this.redisTemplate = redisTemplate;
        this.gameRepository = gameRepository;
    }

    public List<Game> getWaitingGames() {
        return gameRepository.findAllByStatus(Game.Status.WAITING);
    }

    public Game getGame(String id) {
        return gameRepository.findById(id).get();
    }

    public Game addGame(Game game) {
        game.setId(
                Objects.requireNonNull(redisTemplate.opsForValue().increment("playerId"))
                        .toString()
        );
        return gameRepository.save(game);
    }

    public Game move(String id, Move move) {
        Game game = gameRepository.findById(id).get();
        game.move(move.getPlayer(), move.getIndex());
        return gameRepository.save(game);
    }

    public Game join(String id, String playerId) {
        Game game = gameRepository.findById(id).get();
        if (!playerId.equals(game.getFirstPlayer())) {
            game.setSecondPlayer(playerId);
            game.setStatus(Game.Status.STARTED);
        }
        return gameRepository.save(game);
    }
}
