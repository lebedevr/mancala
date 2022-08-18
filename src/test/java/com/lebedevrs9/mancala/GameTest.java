package com.lebedevrs9.mancala;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.lebedevrs9.mancala.model.Game;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Random;
import java.util.stream.Stream;

public class GameTest {

    @Test
    void checkInitialState() {
        assertArrayEquals(
                new int[]{
                        0,
                        6, 6, 6, 6, 6, 6,
                        0,
                        6, 6, 6, 6, 6, 6
                },
                new Game().getBoard()
        );
    }

    @ParameterizedTest
    @MethodSource("firstMove")
    void checkStateAfterFirstMove(int index, int[] expected) {
        Game game = new Game();
        game.move(1, index);
        assertArrayEquals(expected, game.getBoard());
    }

    @Test
    void checkPlayerHasOneMoreMove() {
        Game game = new Game();
        assertEquals(1, game.getCurrentPlayer());
        game.move(1, 1);
        assertEquals(1, game.getCurrentPlayer());
    }

    @Test
    void checkSwitchPlayer() {
        Game game = new Game();
        assertEquals(1, game.getCurrentPlayer());
        game.move(1, 2);
        assertEquals(2, game.getCurrentPlayer());
    }

    @Test
    void checkSteal() {
        Game game = new Game();
        game.move(1, 1);
        game.move(1, 2);
        game.move(2, 8);
        game.move(1, 1);
        assertArrayEquals(
                new int[]{
                        1,
                        0, 0, 8, 8, 8, 8,
                        10,
                        0, 8, 7, 7, 0, 7
                },
                game.getBoard()
        );
    }

    @ParameterizedTest
    @MethodSource("invalidMoves")
    void checkInvalidMove(int player, int[] invalidMoves) {
        Game game = new Game();
        for (int invalidMove : invalidMoves)
            assertFalse(game.move(player, invalidMove));
    }

    @Test
    void checkEmptyPitMove() {
        Game game = new Game();
        assertTrue(game.move(1, 1));
        assertFalse(game.move(1, 1));
    }

    @Test
    void checkCurrentPlayerMove() {
        Game game = new Game();
        assertFalse(game.move(2, 13));
        assertTrue(game.move(1, 2));
        assertTrue(game.move(2, 13));
    }

    @RepeatedTest(1_000)
    void checkEndGameState() {
        Game game = new Game();
        while (!game.isGameEnd()) {
            while (!game.move(game.getCurrentPlayer(), getRandomMove(game.getCurrentPlayer()))) ;
        }
        int[] board = game.getBoard();
        for (int i = 13; i >= 8; i--)
            assertEquals(0, board[i]);
        for (int i = 1; i <= 6; i++)
            assertEquals(0, board[i]);
        assertEquals(72, board[0] + board[7]);
    }

    private static Stream<Arguments> firstMove() {
        return Stream.of(
                Arguments.of(
                        1,
                        new int[]{
                                0,
                                0, 7, 7, 7, 7, 7,
                                1,
                                6, 6, 6, 6, 6, 6
                        }
                ),
                Arguments.of(
                        2,
                        new int[]{
                                0,
                                6, 0, 7, 7, 7, 7,
                                1,
                                7, 6, 6, 6, 6, 6
                        }
                ),
                Arguments.of(
                        3,
                        new int[]{
                                0,
                                6, 6, 0, 7, 7, 7,
                                1,
                                7, 7, 6, 6, 6, 6
                        }
                ),
                Arguments.of(
                        4,
                        new int[]{
                                0,
                                6, 6, 6, 0, 7, 7,
                                1,
                                7, 7, 7, 6, 6, 6
                        }
                ),
                Arguments.of(
                        5,
                        new int[]{
                                0,
                                6, 6, 6, 6, 0, 7,
                                1,
                                7, 7, 7, 7, 6, 6
                        }
                ),
                Arguments.of(
                        6,
                        new int[]{
                                0,
                                6, 6, 6, 6, 6, 0,
                                1,
                                7, 7, 7, 7, 7, 6
                        }
                )
        );
    }

    private static Stream<Arguments> invalidMoves() {
        return Stream.of(
                Arguments.of(
                        1,
                        new int[]{0, 7, 8, 9, 10, 11, 12, 13}
                ),
                Arguments.of(
                        2,
                        new int[]{0, 1, 2, 3, 4, 5, 6, 7}
                )
        );
    }

    private int getRandomMove(int player) {
        int r = new Random().nextInt(Game.playerSideSize);
        int i = 0;
        for (int index : Game.indexesByPlayer.get(player)) {
            if (i == r) return index;
            i++;
        }
        return 0;
    }
}
