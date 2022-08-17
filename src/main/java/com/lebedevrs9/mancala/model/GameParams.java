package com.lebedevrs9.mancala.model;

import java.util.Map;
import java.util.Set;

public enum GameParams {

    INSTANCE;

    public final Map<Integer, Set<Integer>> indexesByPlayer = Map.of(
            1, Set.of(1, 2, 3, 4, 5, 6),
            2, Set.of(8, 9, 10, 11, 12, 13)
    );

    public final int boardSize = 14;
    public final int playerSideSize = 6;
}
