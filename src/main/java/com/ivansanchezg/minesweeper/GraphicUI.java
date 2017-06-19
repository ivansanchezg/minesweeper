package com.ivansanchezg.minesweeper;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.Timer;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.Image;
import javax.imageio.ImageIO;

import java.io.IOException;

public class GraphicUI {
    private JFrame frame;
    private JPanel panel;
    private Board board;
    private int rows;
    private int cols;
    private int flagsCounter;

    private int tileSize = 20;
    private JButton[][] buttonsMatrix;

    private int widthOffset = tileSize/2;
    private int heightOffset = 75;

    private Image flag;
    private JLabel flagsLabel;
    private JLabel timeLabel;

    private int time;
    private Timer timer;

    public GraphicUI() {
        frame = new JFrame("MineSweeper");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        panel = new JPanel();
        panel.setLayout(null);
        frame.add(panel);
        initialize(10, 10);
    }

    public void initialize(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.flagsCounter = 0;
        this.time = 0;
        board = new Board(rows,cols);
        buttonsMatrix = new JButton[rows][cols];
        initializeWindow();
        initializeGraphicalComponents();
        displayWindow();
        startTimeCount();
    }

    private void initializeWindow() {
        frame.setMinimumSize(new Dimension((cols + 1) * tileSize, (rows + 2) * tileSize + heightOffset));
    }

    private void startTimeCount() {
        if(timer != null) {
            timer.stop();
        }
        ActionListener taskPerformer = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                time += 1;
                timeLabel.setText("Time: " +  time);
            }
        };
        timer = new Timer(1000, taskPerformer);
        timer.start();
    }

    private void initializeGraphicalComponents() {
        try {
            flag = ImageIO.read(getClass().getResource("/images/flag.png"));
        } catch(IOException e) {
            System.out.println("IOException. Couldn't load image'");
            System.exit(1);
        } catch(Exception e) {
            System.out.println("Exception. Couldn't load image'");
            System.exit(1);
        }
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

        // Flags
        flagsLabel = new JLabel("Flags: " + String.valueOf(board.getMines() - flagsCounter));
        flagsLabel.setBounds(10, 40, 100, 20);
        panel.add(flagsLabel);

        // Time
        timeLabel = new JLabel("Time: " + time);
        timeLabel.setBounds(120, 40, 100, 20);
        panel.add(timeLabel);
    }

    private void initializeButtons(JPanel panel, int rows, int cols) {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                JButton button = new JButton();
                button.setEnabled(true);
                button.setBounds((col * tileSize) + widthOffset, 
                                 (row * tileSize) + heightOffset, 
                                 tileSize, tileSize);
                //button.addActionListener(new TileActionListener(button, board, this));
                button.addMouseListener(new ButtonMouseListener(button, board, this));
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
    
        if (board.isGameOver() || board.playerWon()) {
            for (int row = 0; row < rows; row++) {
                for (int col = 0; col < cols; col++) {
                    buttonsMatrix[row][col].setEnabled(false);
                }
            }
        }
        
        //timeLabel.setText("Time: " +  this.time);
        flagsLabel.setText("Flags: " + String.valueOf(board.getMines() - flagsCounter));
        panel.repaint();
    }

    public void toggleFlag(int row, int col) {
        if (buttonsMatrix[row][col].getIcon() == null) {
            buttonsMatrix[row][col].setIcon(new ImageIcon(flag));
            flagsCounter++;
        } else {
            buttonsMatrix[row][col].setIcon(null);
            flagsCounter--;
        }
        checkRevealResult(); //?
    }

    private class ButtonMouseListener implements MouseListener {
        private JButton button;
        private Board board;
        private GraphicUI graphicUI;

        public ButtonMouseListener(JButton button, Board board, GraphicUI graphicUI) {
            this.button = button;
            this.board = board;
            this.graphicUI = graphicUI;
        }

        public void mouseClicked(MouseEvent event) {
            int row = (button.getY() - heightOffset)/tileSize;
            int col = (button.getX() - widthOffset)/tileSize;
            //System.out.println("Clicked on button " + row + "," + col);
            if(SwingUtilities.isLeftMouseButton(event)) {
                //System.out.println("Left mouse pressed!");
                board.reveal(row, col);
                graphicUI.checkRevealResult();
            } else if(SwingUtilities.isRightMouseButton(event)) {
                //System.out.println("Right mouse pressed!");
                graphicUI.toggleFlag(row, col);
            }
        }

        public void mouseEntered(MouseEvent event) {}
        public void mouseExited(MouseEvent event) {}
        public void mousePressed(MouseEvent event) {}
        public void mouseReleased(MouseEvent event) {}
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

    // private class TileActionListener implements ActionListener {
    //     private JButton button;
    //     private Board board;
    //     private GraphicUI graphicUI;

    //     public TileActionListener(JButton button, Board board, GraphicUI graphicUI) {
    //         this.button = button;
    //         this.board = board;
    //         this.graphicUI = graphicUI;
    //     }
    //     public void actionPerformed(ActionEvent e) {
    //         System.out.println("actionPerformed");
    //         int row = (button.getY() - heightOffset)/tileSize;
    //         int col = (button.getX() - widthOffset)/tileSize;
    //         System.out.println("Clicked on button " + row + "," + col);
    //         board.reveal(row, col);
    //         graphicUI.checkRevealResult();
    //     }
    // }
}