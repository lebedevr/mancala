package com.lebedevrs9.mancala.model;

import lombok.Data;

@Data
public class Move {
    private final int player;
    private final int index;
}
