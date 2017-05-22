package com.ivansanchezg.minesweeper;

public class Board {
    Tile[][] tiles;
    private int rows;
    private int cols;
    private int mines;
    private int tilesRevealed;
    private boolean gameOver;

    public Board(int rows, int cols) {
        this.cols = cols;
        this.rows = rows;
        mines = (int) Math.sqrt(cols * rows);
        tiles = new Tile[rows][cols];
        init();
    }

    public void init() {
        gameOver = false;
        tilesRevealed = 0;
        resetTiles();
        generateMines();
    }

    private void resetTiles() {
        for(int row = 0; row < rows; row++) {
            for(int col = 0; col < cols; col++) {
                tiles[row][col] = new Tile(0);
            }
        }
    }

    private void generateMines() {
        int row, col;
        for(int i = 0; i < mines; i++) {
            do {
                row = (int) (Math.random() * rows);
                col = (int) (Math.random() * cols);
            } while(tiles[row][col].getValue() == -1);
            tiles[row][col] = new Tile(-1);
        }
    }

    public boolean inRange(int row, int col) {
        if (row < 0 || col < 0) {
            return false;
        }
        if (row >= rows || col >= cols) {
            return false;
        }
        return true;
    }

    public boolean isRevealed(int row, int col) {
        return tiles[row][col].isRevealed();
    }

    public void reveal(int row, int col) {
        if(tiles[row][col].getValue() == -1) {
            tiles[row][col].reveal();
            gameOver = true;
        } else {
            tilesRevealed += 1;
            tiles[row][col].reveal();
            int minesAround = countMinesCountAround(row, col);            
            if (minesAround > 0) {
                tiles[row][col].setValue(minesAround);
            } else {
                for(int i = row - 1; i <= row + 1; i++) {
                    for(int j = col - 1; j <= col + 1; j++) {
                        if(inRange(i, j) && !(i == row && j == col)) {
                            if(!tiles[i][j].isRevealed()) {
                                reveal(i,j);
                            }
                        }
                    }
                }
            }
        }
    }

    public void toggleDisplayValue(int row, int col, char value) {
        if (tiles[row][col].getDisplayValue() == value) {
            tiles[row][col].setDisplayValue(' ');
        } else {
            tiles[row][col].setDisplayValue(value);
        }
    }

    private int countMinesCountAround(int row, int col) {
        int count = 0;
        for(int i = row - 1; i <= row + 1; i++) {
            for(int j = col - 1; j <= col + 1; j++) {
                if(inRange(i, j) && !(i == row && j == col)) {
                    if (tiles[i][j].getValue() == -1) {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public boolean playerWon() {
        return tilesRevealed == (rows * cols) - mines;
    }

    public Tile[][] getTiles() {
        return tiles;
    }

    public int getTilesRevealed() {
        return tilesRevealed;
    }

    public int getMines() {
        return mines;
    }
}