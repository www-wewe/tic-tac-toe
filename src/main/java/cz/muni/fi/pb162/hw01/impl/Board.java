package cz.muni.fi.pb162.hw01.impl;

/**
 * The class represents the board, consisting of cells stored in the 2D array whose size is size x size.
 * The required count of symbols for the win is in the attribute win, and attribute history represents
 * the count of turns that you can rewind. There is a history list with an index pointing to the actual turn.
 *
 * @author Veronika Lenkova
 */
public class Board implements BoardFormatter {
    private final int size;
    private final int win;
    private final int history;
    private Character winner;

    private int historyIndex = 0;
    private int[][] historyList;

    private Character[][] gameBoard;

    /**
     * Creates the board with
     *
     * @param size    NxN
     * @param win     required count
     * @param history size of history list
     */
    public Board(int size, int win, int history) {
        this.size = size;
        this.win = win;
        this.history = history;
        this.historyList = new int[history][2];
        this.gameBoard = new Character[size][size];
    }

    public Character getWinner() {
        return winner;
    }

    public void setWinner(Character winner) {
        this.winner = winner;
    }

    /**
     * Places a symbol on given coordinates.
     *
     * @param x      row
     * @param y      column
     * @param symbol symbol
     */
    public void put(int x, int y, char symbol) {
        gameBoard[x][y] = symbol;
        saveToHistory(x, y);
    }

    /**
     * Removes symbol from board.
     *
     * @param x row
     * @param y col
     */
    public void remove(int x, int y) {
        gameBoard[x][y] = null;
    }

    /**
     * Stores the actual turn into history list.
     *
     * @param x row
     * @param y col
     */
    void saveToHistory(int x, int y) {
        historyList[(historyIndex) % history] = new int[]{x, y};
        historyIndex++;
    }

    /**
     * Removes last N placed symbols.
     *
     * @param num of rewind
     */
    void getFromHistory(int num) {
        for (int i = 0; i < num; i++) {
            int x = historyList[(historyIndex - 1) % history][0];
            int y = historyList[(historyIndex - 1) % history][1];
            remove(x, y);
            historyIndex--;
        }
    }

    /**
     * @param x row
     * @param y column
     * @return true if cell is empty
     */
    boolean isEmpty(int x, int y) {
        return gameBoard[x][y] == null;
    }

    /**
     * @return true if the board is completely filled by symbols
     */
    boolean isFull() {
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                if (isEmpty(x, y)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Determines if the last turn was the winning one.
     * (if there is a required count of the same symbols in a line)
     *
     * @param player symbol
     * @param x      row
     * @param y      column
     * @return true if is the end of game
     */
    boolean isWinner(Character player, int x, int y) {
        int winInRow = 0;
        int winInCol = 0;
        int winInFirstDiagonal = 0;
        int winInSecondDiagonal = 0;

        for (int z = 0; z < size; z++) {
            if (!isEmpty(x, z) && gameBoard[x][z].equals(player)) {
                winInRow++;
            }
            if (!isEmpty(z, y) && gameBoard[z][y].equals(player)) {
                winInCol++;
            }
            if (!isEmpty(z, z) && gameBoard[z][z].equals(player)) {
                winInFirstDiagonal++;
            }
            if (!isEmpty(size - 1 - z, z) && gameBoard[size - 1 - z][z].equals(player)) {
                winInSecondDiagonal++;
            }
        }

        if (winInCol == win || winInRow == win || winInFirstDiagonal == win || winInSecondDiagonal == win) {
            setWinner(player);
            return true;
        }
        return false;
    }

    @Override
    public String format(Board board) {
        StringBuilder buffer = new StringBuilder(("-".repeat(size * 2 + 1) + '\n'));
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                buffer.append('|');
                if (isEmpty(x, y)) {
                    buffer.append(' ');
                } else {
                    buffer.append(board.gameBoard[x][y]);
                }
            }
            buffer.append("|\n").append("-".repeat(size * 2 + 1)).append('\n');
        }
        return buffer.toString();
    }

    /**
     * ...
     */
    public void draw() {
        System.out.print(format(this));
    }

}
