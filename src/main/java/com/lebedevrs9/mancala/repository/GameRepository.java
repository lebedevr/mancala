package com.lebedevrs9.mancala.repository;

import com.lebedevrs9.mancala.model.Game;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameRepository extends CrudRepository<Game, String> {
    List<Game> findAllByStatus(Game.Status status);
}