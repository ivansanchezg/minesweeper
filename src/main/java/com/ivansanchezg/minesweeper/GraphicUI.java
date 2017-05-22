package com.ivansanchezg.minesweeper;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Dimension;

public class GraphicUI {
    private JFrame frame;
    private JPanel panel;
    private Board board;

    private int tileSize = 20;

    public GraphicUI() {
        initializeWindow();
        board = new Board(10,10);
        initializeGraphicalComponents();
        displayWindow();
    }

     private void initializeWindow() {
        frame = new JFrame("MineSweeper");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setBackground(Color.RED);
        frame.setMinimumSize(new Dimension(200, 250));
    }

    private void initializeGraphicalComponents() {
        panel = new JPanel();
        panel.setLayout(null);
        panel.setSize(200, 200);
        draw();
        frame.add(panel);
    }

    private void displayWindow() {
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void checkRevealResult() {
        if (board.isGameOver()) {
            System.out.println("GAME OVER!!!");
        } else if (board.playerWon()) {
            System.out.println("YOU WIN!!!");
        }
        draw();
    }

    private void draw() {
        panel.removeAll();
        boolean gameOver = board.isGameOver();
        boolean playerWon = board.playerWon();

        Tile[][] tiles = board.getTiles();
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                if (tiles[row][col].isRevealed()) {
                    JLabel label = new JLabel();
                    label.setBounds(col * tileSize, row * tileSize, tileSize, tileSize);
                    label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                    label.setText(String.valueOf(tiles[row][col].getValue()));
                    label.setHorizontalAlignment(SwingConstants.CENTER);
                    panel.add(label);
                } else {
                    JButton button = new JButton();
                    button.setEnabled(!gameOver && !playerWon);
                    button.setBounds(col * tileSize, row * tileSize, tileSize, tileSize);
                    button.addActionListener(new ButtonActionListener(button, board, this));
                    panel.add(button);
                }
            }
        }
        panel.repaint();
    }

    private class ButtonActionListener implements ActionListener {
        private JButton button;
        private Board board;
        private GraphicUI graphicUI;

        public ButtonActionListener(JButton button, Board board, GraphicUI graphicUI) {
            this.button = button;
            this.board = board;
            this.graphicUI = graphicUI;
        }
        public void actionPerformed(ActionEvent e) {
            int row = button.getY()/tileSize;
            int col = button.getX()/tileSize;
            System.out.println("\nClicked on button " + row + "," + col);
            board.reveal(row, col);
            graphicUI.checkRevealResult();
        }
    }
}