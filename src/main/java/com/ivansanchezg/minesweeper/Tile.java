package com.ivansanchezg.minesweeper;

public class Tile {
    private int value;
    private char displayValue;
    private boolean revealed;

    public Tile(int value) {
        this.value = value;
        this.displayValue = ' ';
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public char getDisplayValue() {
        return displayValue;
    }

    public void setDisplayValue(char displayValue) {
        this.displayValue = displayValue;
    }

    public boolean isRevealed() {
        return revealed;
    }

    public void reveal() {
        revealed = true;
    }
}
