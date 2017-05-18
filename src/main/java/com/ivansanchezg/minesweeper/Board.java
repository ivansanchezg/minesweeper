package com.ivansanchezg.minesweeper;

import java.util.Scanner;

public class Board {
    Tile[][] tiles;
    private int rows;
    private int cols;
    private int mines;
    private int tilesRevealed;
    private boolean gameOver;
    Scanner scanner;

    public Board() {
        cols = 10;
        rows = 10;
        mines = 10;
        tilesRevealed = 0;
        tiles = new Tile[rows][cols];
        scanner = new Scanner(System.in);
        init();
    }

    private void init() {
        gameOver = false;
        resetTiles();
        generateMines();
        startGame();
    }

    private void resetTiles() {
        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < cols; j++) {
                tiles[i][j] = new Tile(0);
            }
        }
    }

    private void generateMines() {
        int x;
        int y;
        for(int i = 0; i < mines; i++) {
            do {
                x = (int) (Math.random() * rows);
                y = (int) (Math.random() * cols);
            } while(tiles[x][y].getValue() == -1);
            System.out.println("Mine set at: " + x + "," + y);
            tiles[x][y] = new Tile(-1);
        }
    }

    private void startGame() {
        while(!gameOver && tilesRevealed < (rows * cols) - mines) {
            int[] input = readInput();
            reveal(input[0], input[1]);
        }
        if(gameOver) {
            System.out.println("Game Over");
        } else {
            System.out.println("You won");
        }        
    }

    private int[] readInput() {
        int x;
        int y;
        do {
            System.out.print("Type row. Values from 0 to " + (rows - 1) + ": ");
            x = scanner.nextInt();
            System.out.print("Type col. Values from 0 to " + (cols - 1) + ": ");
            y = scanner.nextInt();
        } while(!isValidInput(x, y));
        return new int[] { x, y };
    }

    private boolean isValidInput(int x, int y) {
        if (!inRange(x, y)) {
            System.out.println("Values are not in range of the board");
        }
        if (tiles[x][y].isRevealed()) {
            System.out.println("That tile has already been revelead, select another one");
            return false;
        }        
        return true;
    }

    private boolean inRange(int x, int y) {
        if (x < 0 || y < 0) {
            return false;
        }
        if (x >= rows || y >= cols) {
            return false;
        }
        return true;
    }

    private void reveal(int x, int y) {
        if(tiles[x][y].getValue() == -1) {
            tiles[x][y].reveal();
            gameOver = true;
        } else {
            tilesRevealed += 1;
            tiles[x][y].reveal();
            int minesAround = countMinesCountAround(x, y);            
            if (minesAround > 0) {
                tiles[x][y].setValue(minesAround);
            } else {
                for(int i = x - 1; i <= x + 1; i++) {
                    for(int j = y - 1; j <= y + 1; j++) {
                        if(inRange(i, j) && !(i == x && j == y)) {
                            if(!tiles[i][j].isRevealed()) {
                                reveal(i,j);
                            }
                        }
                    }
                }
            }
        }
        printBoard();
    }

    private void printBoard() {
        System.out.println("");
        System.out.print(" ");
        for(int i = 0; i < cols; i++) {
            System.out.print(" " + i);
        }
        System.out.println("");
        for (int i = 0; i < rows; i++) {
            System.out.print(i + " ");
            for (int j = 0; j < cols; j++) {
                if (tiles[i][j].isRevealed()) {
                    if (tiles[i][j].getValue() >= 0) {
                        System.out.print(tiles[i][j].getValue() + " ");
                    } else {
                        System.out.print("X ");
                    }
                } else {
                    System.out.print("- ");
                }
            }
            System.out.print("\n");
        }
    }

    private int countMinesCountAround(int x, int y) {
        int count = 0;
        //System.out.println("Counting mines");
        for(int i = x - 1; i <= x + 1; i++) {
            for(int j = y - 1; j <= y + 1; j++) {
                //System.out.println("Checking: " + i + "," + j);
                if(inRange(i, j) && !(i == x && j == y)) {
                    //System.out.println(i + "," + j + " is in range and is different from " + x + "," + y);
                    //System.out.println("Value: " + tiles[i][j].getValue());
                    if (tiles[i][j].getValue() == -1) {
                        count++;
                    }
                }
            }
        }
        return count;
    }
}