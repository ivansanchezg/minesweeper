package com.ivansanchezg.minesweeper;

import java.util.Scanner;

public class ConsoleUI {
    private Scanner scanner;
    private Board board;
    private int cols;
    private int rows;

    public ConsoleUI() {
        scanner = new Scanner(System.in);
        this.cols = 10;
        this.rows = 10;
        board = new Board(rows, cols);
        startGame();
    }

    private void startGame() {
        while (!board.isGameOver() && !board.playerWon()) {
            readInput();
            printBoard();
        }
        if (board.isGameOver()) {
            System.out.println("Game Over");
        } else if (board.playerWon()) {
            System.out.println("You won");
        }
        System.out.print("Play again? Y/N: ");
        String response = scanner.next();
        if (response.equalsIgnoreCase("y")) {
            board.init();
            startGame();
        }
    }

    private void readInput() {
        int row, col;
        String line;
        do {
            System.out.print("Input: ");
            line = scanner.next();
            if(line.length() == 3) {
                row = Character.getNumericValue(line.charAt(0));
                col = Character.getNumericValue(line.charAt(2));
            } else if(line.length() == 4) {
                if(line.charAt(0) != 'F' && line.charAt(0) != '?') {
                    row = -1;
                    col = -1;    
                } else {
                    row = Character.getNumericValue(line.charAt(1));
                    col = Character.getNumericValue(line.charAt(3));
                }
            } else {
                row = -1;
                col = -1;
            }
        } while(!isValidInput(row, col));
        if(line.length() == 3) {
            board.reveal(row, col);
        } else if(line.length() == 4) {
            board.toggleDisplayValue(row, col, line.charAt(0));
        }
    }

    private boolean isValidInput(int row, int col) {
        if (!board.inRange(row, col)) {
            System.out.println("Values are not in range of the board");
            return false;
        }
        if (board.isRevealed(row, col)) {
            System.out.println("That tile has already been revelead, select another one");
            return false;
        }        
        return true;
    }

    //Prints the board on the console
    private void printBoard() {
        Tile[][] tiles = board.getTiles();
        System.out.println("");
        System.out.print("   ");
        for(int num = 0; num < cols; num++) {
            System.out.print(" " + num);
        }
        System.out.println("");
        for(int num = 0; num < cols * 2 + 3; num++) {
            System.out.print("-");
        }
        System.out.println("");
        for (int row = 0; row < rows; row++) {
            System.out.print(row + " | ");
            for (int col = 0; col < cols; col++) {
                if (tiles[row][col].isRevealed()) {
                    if (tiles[row][col].getValue() >= 0) {
                        System.out.print(tiles[row][col].getValue() + " ");
                    } else {
                        System.out.print("X ");
                    }
                } else {
                    if(tiles[row][col].getDisplayValue() == ' ') {
                        System.out.print("- ");
                    } else {
                        System.out.print(tiles[row][col].getDisplayValue() + " ");
                    }
                }
            }
            System.out.print("\n");
        }
    }
}