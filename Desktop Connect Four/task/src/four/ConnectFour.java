package four;

import javax.swing.*;
import java.awt.*;

public class ConnectFour extends JFrame {
    private static char currentPlayer = 'X';
    private final JButton[][] buttons = new JButton[6][7];
    private static final Color BASELINE_COLOR = Color.LIGHT_GRAY;
    private static final Color WINNING_COLOR = Color.CYAN;
    private boolean gameWon = false;

    public ConnectFour() {
        setTitle("Connect Four");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        initCells();
        addResetButton();
        setVisible(true);

    }

    private void initCells() {
        JPanel cellsPanel = new JPanel();
        cellsPanel.setBounds(0, 0, getWidth(), getHeight());
        cellsPanel.setLayout(new GridLayout(6, 7));

        char[] letters = {'A', 'B', 'C', 'D', 'E', 'F', 'G'};
        for (int i = 0; i < 6; i++) { // iterate over rows from top to bottom
            JButton[] row = new JButton[7];
            for (int j = 0; j < 7; j++) { // iterate over columns from left to right
                JButton button = new JButton();
                button.setFocusPainted(false);
                button.setBackground(BASELINE_COLOR);
                button.setName("Button" + letters[j] + "" + (6 - i)); // set button name
                button.setText(" "); // set button text
                buttons[i][j] = button; // add the button to the array
                row[j] = button;
                cellsPanel.add(button);

                // attach an action listener to the button
                button.addActionListener(e -> {
                    if (gameWon) { // do nothing if the game has been won
                        return;
                    }
                    JButton clickedButton = (JButton) e.getSource();
                    int column = getColumn(clickedButton); // get the column index of the clicked button
                    int rowIdx = getEmptyRowInColumn(column); // get the row index of the empty cell in the column
                    if (rowIdx == -1) {
                        // if the column is full, do nothing and return
                        return;
                    }
                    buttons[rowIdx][column].setText(String.valueOf(currentPlayer)); // set the text of the empty cell in the column to the player's symbol
                    if (!isWinCondition()) {
                        changePlayer();
                    } else {
                        gameWon = true;
                        disableButtons();
                    }
                });
            }
        }
        add(cellsPanel, BorderLayout.CENTER);
    }


    private boolean isWinCondition() {
        // Check for horizontal wins
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 4; col++) {
                char c = buttons[row][col].getText().charAt(0);
                if (c != ' ' && c == buttons[row][col + 1].getText().charAt(0) && c == buttons[row][col + 2].getText().charAt(0) && c == buttons[row][col + 3].getText().charAt(0)) {
                    highlightWinningCells(row, col, row, col + 1, row, col + 2, row, col + 3);
                    return true;
                }
            }
        }

        // Check for vertical wins
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 7; col++) {
                char c = buttons[row][col].getText().charAt(0);
                if (c != ' ' && c == buttons[row + 1][col].getText().charAt(0) && c == buttons[row + 2][col].getText().charAt(0) && c == buttons[row + 3][col].getText().charAt(0)) {
                    highlightWinningCells(row, col, row + 1, col, row + 2, col, row + 3, col);
                    return true;
                }
            }
        }

        // Check for diagonal wins (from top-left to bottom-right)
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 4; col++) {
                char c = buttons[row][col].getText().charAt(0);
                if (c != ' ' && c == buttons[row + 1][col + 1].getText().charAt(0) && c == buttons[row + 2][col + 2].getText().charAt(0) && c == buttons[row + 3][col + 3].getText().charAt(0)) {
                    highlightWinningCells(row, col, row + 1, col + 1, row + 2, col + 2, row + 3, col + 3);
                    return true;
                }
            }
        }

        // Check for diagonal wins (from top-right to bottom-left)
        for (int row = 0; row < 3; row++) {
            for (int col = 3; col < 7; col++) {
                char c = buttons[row][col].getText().charAt(0);
                if (c != ' ' && c == buttons[row + 1][col - 1].getText().charAt(0) && c == buttons[row + 2][col - 2].getText().charAt(0) && c == buttons[row + 3][col - 3].getText().charAt(0)) {
                    highlightWinningCells(row, col, row + 1, col - 1, row + 2, col - 2, row + 3, col - 3);
                    return true;
                }
            }
        }

        return false;
    }

    private void highlightWinningCells(int row1, int col1, int row2, int col2, int row3, int col3, int row4, int col4) {
        buttons[row1][col1].setBackground(WINNING_COLOR);
        buttons[row2][col2].setBackground(WINNING_COLOR);
        buttons[row3][col3].setBackground(WINNING_COLOR);
        buttons[row4][col4].setBackground(WINNING_COLOR);
    }

    private int getColumn(JButton button) {
        // get the column index of the button
        String name = button.getName();
        char letter = name.charAt(6);
        return letter - 'A';
    }

    private int getEmptyRowInColumn(int column) {
        // get the row index of the empty cell in the column
        for (int i = 5; i >= 0; i--) {
            if (buttons[i][column].getText().equals(" ")) {
                return i;
            }
        }
        return -1; // if the column is full, return -1
    }

    private void changePlayer() {
        currentPlayer = (currentPlayer == 'X' ? 'O' : 'X');
    }

    private void addResetButton() {
        JPanel footerPanel = new JPanel();
        footerPanel.setLayout(new BorderLayout());
        // create a reset button and a panel to hold it
        JButton resetButton = new JButton("Reset");
        resetButton.setName("ButtonReset");
        resetButton.setText("Reset");
        resetButton.setFocusPainted(false);
        resetButton.addActionListener(e -> resetGame());
        footerPanel.add(resetButton, BorderLayout.EAST);
        add(footerPanel, BorderLayout.SOUTH);
    }


    private void resetGame() {
        Component[] components = getContentPane().getComponents();
        for (Component component : components) {
            if (component instanceof JPanel cellsPanel) {
                for (Component cell : cellsPanel.getComponents()) {
                    if (cell instanceof JButton button) {
                        if (!button.getText().equals("Reset")) {
                            button.setText(" ");
                            button.setBackground(BASELINE_COLOR);
                            button.setEnabled(true);
                        }
                    }
                }
            }
        }
        currentPlayer = 'X';
        gameWon = false;
        repaint();
    }

    private void disableButtons() {
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                buttons[i][j].setEnabled(false); // disable all the buttons
            }
        }
    }
}