package com.ivansanchezg.minesweeper;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
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
    private int rows;
    private int cols;

    private int tileSize = 20;
    private JButton[][] buttonsMatrix;

    private int widthOffset = tileSize/2;
    private int heightOffset = 75;

    JLabel minesLabel;
    JLabel tilesRemainingLabel;

    public GraphicUI() {
        frame = new JFrame("MineSweeper");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(true);
        frame.setBackground(Color.RED);
        panel = new JPanel();
        panel.setLayout(null);
        frame.add(panel);
        initialize(10, 10);
    }

    public void initialize(int rows, int cols) {
        System.out.println("Initialize: " + rows + ", " + cols);
        this.rows = rows;
        this.cols = cols;
        board = new Board(rows,cols);
        buttonsMatrix = new JButton[rows][cols];
        initializeWindow();
        initializeGraphicalComponents();
        displayWindow();
    }

     private void initializeWindow() {
        frame.setMinimumSize(new Dimension((cols + 1) * tileSize, (rows + 2) * tileSize + heightOffset));
    }

    private void initializeGraphicalComponents() {
        panel.removeAll();
        panel.setSize((cols + 1) * tileSize, (rows + 2) * tileSize + heightOffset);
        initializeConfig(panel);
        initializeButtons(panel, rows, cols);
        draw();
    }

    private void initializeConfig(JPanel panel) {
        JLabel hLabel = new JLabel("H:");
        hLabel.setBounds(10, 10, 15, 20);
        panel.add(hLabel);

        JTextField hTextField = new JTextField();
        hTextField.setText(String.valueOf(rows));
        hTextField.setBounds(30, 10, 40, 20);
        panel.add(hTextField);
    
        JLabel wLabel = new JLabel("W:");
        wLabel.setBounds(80, 10, 15, 20);
        panel.add(wLabel);

        JTextField wTextField = new JTextField();
        wTextField.setText(String.valueOf(cols));
        wTextField.setBounds(100, 10, 40, 20);
        panel.add(wTextField);

        JButton newButton = new JButton("NEW");
        newButton.setBounds(150, 10, 50, 20);
        newButton.addActionListener(new NewGameActionListener(hTextField, wTextField, this));
        panel.add(newButton);

        minesLabel = new JLabel("Mines: " + board.getMines());
        minesLabel.setBounds(10, 40, 100, 20);
        panel.add(minesLabel);

        tilesRemainingLabel = new JLabel("Tiles: " + (rows * cols - board.getTilesRevealed() - board.getMines()));
        tilesRemainingLabel.setBounds(120, 40, 100, 20);
        panel.add(tilesRemainingLabel);
    }

    private void initializeButtons(JPanel panel, int rows, int cols) {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                JButton button = new JButton();
                button.setEnabled(true);
                button.setBounds((col * tileSize) + widthOffset, 
                                 (row * tileSize) + heightOffset, 
                                 tileSize, tileSize);
                button.addActionListener(new TileActionListener(button, board, this));
                buttonsMatrix[row][col] = button;
                panel.add(button);
            }
        }
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
        Tile[][] tiles = board.getTiles();
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (tiles[row][col].isRevealed() && buttonsMatrix[row][col].isEnabled()) {
                    buttonsMatrix[row][col].setEnabled(false);
                    buttonsMatrix[row][col].setBorder(BorderFactory.createLineBorder(Color.BLACK));
                    buttonsMatrix[row][col].setText(String.valueOf(tiles[row][col].getValue()));
                    buttonsMatrix[row][col].setHorizontalAlignment(SwingConstants.CENTER);
                }
            }
        }
    
        if(board.isGameOver() || board.playerWon()) {
            for (int row = 0; row < rows; row++) {
                for (int col = 0; col < cols; col++) {
                    buttonsMatrix[row][col].setEnabled(false);
                }
            }
        }
        
        tilesRemainingLabel.setText("Tiles: " +  (rows * cols - board.getTilesRevealed() - board.getMines()));
        panel.repaint();
    }

    private class NewGameActionListener implements ActionListener {
        private GraphicUI graphicUI;
        private int width;
        private int height;
        private JTextField widthField;
        private JTextField heightField;

        public NewGameActionListener(JTextField newHeight, JTextField newWidth, GraphicUI graphicUI) {
            widthField = newWidth;
            heightField = newHeight;
            this.graphicUI = graphicUI;
        }

        public void actionPerformed(ActionEvent e) {
            try {
                width = Integer.parseInt(widthField.getText());
                height = Integer.parseInt(heightField.getText());
            } catch(NumberFormatException nfe) {
                width = cols;
                height = rows;
            }
            graphicUI.initialize(height, width);
        }
    }

    private class TileActionListener implements ActionListener {
        private JButton button;
        private Board board;
        private GraphicUI graphicUI;

        public TileActionListener(JButton button, Board board, GraphicUI graphicUI) {
            this.button = button;
            this.board = board;
            this.graphicUI = graphicUI;
        }
        public void actionPerformed(ActionEvent e) {
            int row = (button.getY() - heightOffset)/tileSize;
            int col = (button.getX() - widthOffset)/tileSize;
            System.out.println("\nClicked on button " + row + "," + col);
            board.reveal(row, col);
            graphicUI.checkRevealResult();
        }
    }
}