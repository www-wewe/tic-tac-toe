package cz.muni.fi.pb162.hw01.impl;

import com.beust.jcommander.Parameter;
import cz.muni.fi.pb162.hw01.cmd.CommandLine;
import cz.muni.fi.pb162.hw01.cmd.Messages;
import cz.muni.fi.pb162.hw01.Utils;

/**
 * Application class represents the command line interface of this application.
 *
 * @author jcechace, Veronika Lenkova
 */
public class Application {
    @Parameter(names = {"--size", "-s"})
    private int size = 3;

    @Parameter(names = {"--win", "-w"})
    private int win = 3;

    @Parameter(names = {"--history", "-h"})
    private int history = 1;

    @Parameter(names = {"--players", "-p"})
    private String players = "xo";

    @Parameter(names = "--help", help = true)
    private boolean showUsage = false;

    /**
     * Application entry point
     *
     * @param args command line arguments of the application
     */
    public static void main(String[] args) {
        Application app = new Application();

        CommandLine cli = new CommandLine(app);
        cli.parseArguments(args);

        if (app.showUsage) {
            cli.showUsage();
        } else {
            app.run();
        }
    }

    /**
     * Application runtime logic
     */
    private void run() {
        Board gameBoard = new Board(size, win, history);
        int turnsCounter = 1;

        do {
            System.out.printf(Messages.TURN_COUNTER, turnsCounter);
            gameBoard.draw();
            Character actualPlayer = players.charAt((turnsCounter - 1) % players.length());
            System.out.printf(Messages.TURN_PROMPT, actualPlayer);

            String line = Utils.readLineFromStdIn();
            Command command = findCommand(line);
            System.out.print(Messages.TURN_DELIMITER);

            switch (command) {
                case PLAY:
                    int x = Character.getNumericValue(line.charAt(0));
                    int y = Character.getNumericValue(line.charAt(2));
                    if (!turn(gameBoard, actualPlayer, x, y)) {
                        turnsCounter--;
                        break;
                    }
                    if (gameBoard.isWinner(actualPlayer, x, y) || gameBoard.isFull()) {
                        quit(gameBoard, turnsCounter);
                        return;
                    }
                    break;
                case QUIT:
                    quit(gameBoard, turnsCounter - 1);
                    return;
                case REWIND:
                    int num = Character.getNumericValue(line.charAt(2));
                    gameBoard.getFromHistory(num);
                    break;
                default:
                    turnsCounter--;
                    break;
            }
            turnsCounter++;
        } while (true);
    }

    /**
     * The method finds out which {@link Command} is entered during a gameplay.
     * The game responds PLAY = X Y, REWIND = << N, QUIT = :q and other is not valid.
     * X, Y and N must be in <0, size) where size is not bigger than 10.
     *
     * @param line from stdin
     * @return Command
     */
    private Command findCommand(String line) {
        if (line.length() == 3) {
            if (Character.isDigit(line.charAt(0)) && Character.isDigit(line.charAt(2))
                    && line.charAt(1) == (' ')) {
                int x = Character.getNumericValue(line.charAt(0));
                int y = Character.getNumericValue(line.charAt(2));
                if (checkNumber(x, size) && checkNumber(y, size)) {
                    return Command.PLAY;
                }
                System.out.println(Messages.ERROR_ILLEGAL_PLAY);
                return Command.INVALID;
            }
            if (line.charAt(0) == ('<') && line.charAt(1) == ('<') && Character.isDigit(line.charAt(2))) {
                int num = Character.getNumericValue(line.charAt(2));
                if (checkNumber(num, history)) {
                    return Command.REWIND;
                }
                System.out.println(Messages.ERROR_REWIND);
                return Command.INVALID;
            }
        } else if (line.equals(":q")) {
            return Command.QUIT;
        }
        System.out.println(Messages.ERROR_INVALID_COMMAND);
        return Command.INVALID;

    }

    /**
     * Checks if the cell is empty and if yes, puts the symbol associated with
     * the current player at the cell with the entered coordinates.
     *
     * @param gameBoard actual board
     * @param symbol    player
     * @param x         row
     * @param y         col
     * @return true for successful turn
     */
    public boolean turn(Board gameBoard, Character symbol, int x, int y) {
        if (!gameBoard.isEmpty(x, y)) {
            System.out.print(Messages.ERROR_ILLEGAL_PLAY);
            return false;
        }
        gameBoard.put(x, y, symbol);
        return true;
    }

    /**
     * Stops the game, draws the board and if there is the winner, prints it.
     *
     * @param gameBoard actual board
     * @param counter   of turns
     */
    public void quit(Board gameBoard, int counter) {
        System.out.printf(Messages.GAME_OVER, counter);
        gameBoard.draw();
        if (gameBoard.getWinner() != null) {
            System.out.printf(Messages.GAME_WINNER, gameBoard.getWinner());
        }
    }

    /**
     * Checks if the number is in the interval <0, maximum).
     *
     * @param number  checking
     * @param maximum of interval
     * @return boolean
     */
    public boolean checkNumber(int number, int maximum) {
        return number >= 0 && number < maximum;
    }
}
