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
        while(!board.isGameOver() && board.getTilesRevealed() < (rows * cols) - board.getMines()) {
            int[] input = readInput();
            board.reveal(input[0], input[1]);
            printBoard();
        }
        if(board.isGameOver()) {
            System.out.println("Game Over");
        } else {
            System.out.println("You won");
        }
        System.out.print("Play again? Y/N: ");
        String response = scanner.next();
        if(response.equalsIgnoreCase("y")) {
            board.init();
            startGame();
        }
    }

    private int[] readInput() {
        int row, col;
        do {
            System.out.print("Type row. Values from 0 to " + (rows - 1) + ": ");
            row = scanner.nextInt();
            System.out.print("Type col. Values from 0 to " + (cols - 1) + ": ");
            col = scanner.nextInt();
        } while(!isValidInput(row, col));
        return new int[] { row, col };
    }

    private boolean isValidInput(int row, int col) {
        if (!board.inRange(row, col)) {
            System.out.println("Values are not in range of the board");
        }
        if (board.isRevealed(row, col)) {
            System.out.println("That tile has already been revelead, select another one");
            return false;
        }        
        return true;
    }

    private void printBoard() {
        Tile[][] tiles = board.getTiles();
        System.out.println("");
        System.out.print(" ");
        for(int num = 0; num < cols; num++) {
            System.out.print(" " + num);
        }
        System.out.println("");
        for (int row = 0; row < rows; row++) {
            System.out.print(row + " ");
            for (int col = 0; col < cols; col++) {
                if (tiles[row][col].isRevealed()) {
                    if (tiles[row][col].getValue() >= 0) {
                        System.out.print(tiles[row][col].getValue() + " ");
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
}