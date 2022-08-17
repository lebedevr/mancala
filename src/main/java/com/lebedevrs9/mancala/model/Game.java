package com.lebedevrs9.mancala.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;


@Data
public class Game {

    public enum Status {
        WAITING, STARTED
    }

    @JsonIgnore
    private GameParams params = GameParams.INSTANCE;

    private int id;

    private String name;
    private int[] board = {
            0,
            6, 6, 6, 6, 6, 6,
            0,
            6, 6, 6, 6, 6, 6
    };

    private int currentPlayer = 1;

    private String firstPlayer = null;

    private String secondPlayer = null;

    private Integer winner = null;

    private boolean gameEnd = false;

    private Status status = Status.WAITING;

    public synchronized boolean move(int player, int index) {
        if (!validateMove(player, index)) return false;
        int lastIndex = moveStones(index);
        checkForSteal(player, lastIndex);
        if (checkGameEnd()) {
            moveAllToGoal();
            winner = detectWinner();
            gameEnd = true;
            return true;
        }
        if (!hasOneMoreMove(player, lastIndex))
            switchPlayer();
        return true;
    }

    private boolean checkGameEnd() {
        return checkGameEnd(currentPlayer) || checkGameEnd(opponent(currentPlayer));
    }

    private boolean checkGameEnd(int player) {
        for (int i : params.indexesByPlayer.get(player))
            if (board[i] != 0)
                return false;
        return true;
    }

    private boolean validateMove(int player, int index) {
        return currentPlayer == player && params.indexesByPlayer.get(player).contains(index) && board[index] != 0;
    }

    private int moveStones(int index) {
        int stones = board[index];
        board[index] = 0;
        for (int i = index + 1; i <= index + stones; i++) board[i % params.boardSize]++;
        return (index + stones) % params.boardSize;
    }

    private void checkForSteal(int player, int lastIndex) {
        if (params.indexesByPlayer.get(player).contains(lastIndex) &&
                board[lastIndex] == 1 &&
                board[opposite(lastIndex)] != 0) {
            int toAdd = board[opposite(lastIndex)] + 1;
            board[opposite(lastIndex)] = 0;
            board[lastIndex] = 0;
            board[goal(player)] += toAdd;
        }
    }

    private boolean hasOneMoreMove(int player, int lastIndex) {
        return goal(player) == lastIndex;
    }

    private void switchPlayer() {
        currentPlayer = opponent(currentPlayer);
    }

    private void moveAllToGoal() {
        moveAllToGoal(currentPlayer);
        moveAllToGoal(opponent(currentPlayer));
    }

    private void moveAllToGoal(int player) {
        int toAdd = 0;
        for (int i : params.indexesByPlayer.get(player)) {
            toAdd += board[i];
            board[i] = 0;
        }
        board[goal(player)] += toAdd;
    }

    private Integer detectWinner() {
        int currentPlayerScore = board[goal(currentPlayer)];
        int opponent = opponent(currentPlayer);
        int opponentScore = board[goal(opponent)];
        if (currentPlayerScore > opponentScore) return currentPlayer;
        else if (currentPlayerScore < opponentScore) return opponent;
        else return null;
    }

    private int opponent(int player) {
        if (player == 1) return 2;
        else return 1;
    }

    private int goal(int player) {
        if (player == 1) return 7;
        else return 0;
    }

    private int opposite(int index) {
        switch (index) {
            case 1: return 13;
            case 13: return 1;
            case 2: return 12;
            case 12: return 2;
            case 3: return 11;
            case 11: return 3;
            case 4: return 10;
            case 10: return 4;
            case 5: return 9;
            case 9: return 5;
            case 6: return 8;
            case 8: return 6;
            default: throw new IllegalArgumentException();
        }
    }

}
