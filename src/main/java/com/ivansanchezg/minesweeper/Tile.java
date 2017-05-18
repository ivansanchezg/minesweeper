package com.ivansanchezg.minesweeper;

public class Tile {
    private int value;
    private boolean revealed;

    public Tile(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public boolean isRevealed() {
        return revealed;
    }

    public void reveal() {
        revealed = true;
    }
}
