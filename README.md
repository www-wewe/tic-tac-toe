Homework solution, Tic-Tac-Toe
====================================
General information
-------------------
This project is implementation of the game of [tic-tac-toe](https://en.wikipedia.org/wiki/Tic-tac-toe)  
The players will alternate turns, each playing one action command. 
See the "Running the application" section of this README to understand the desired behavior and the user interface. 

Then game ends when either of the following happens:

- There is a required number of the same symbols in a line
- The board runs out of free cells
- The Quit command is executed

### Command Line Option
Application support the following

| Name         | Short | Long      | Default | Description                                       | Conditions        |
|--------------|-------|-----------|---------|---------------------------------------------------|-------------------|
| Size         | -s    | --size    | 3       | Size of the board (NxN)                           | >= 3              |
| Win Size     | -w    | --win     | 3       | Number of symbols in line required  to win        | 3 <= w <= size    |
| History Size | -h    | --history | 1       | Number of turns the player is able to rewind back | 0 < h < size*size |
| Players      | -p    | --players | xo      | Symbols and order of players                      | len(players) > 1  |
| Help         |       | --help    | false   | Prints usage                                      |                   |

### User interface
The example bellow should give a good description how a single turn of the game should be presented.

```
Turn: 1
-------
| | | |
-------
| | | |
-------
| | | |
-------
Enter your turn (x): 1 1
===

Turn: 2
-------
| | | |
-------
| |x| |
-------
| | | |
-------
Enter your turn (o):
```

After the last turn (at the game end) the following should be appended 

```
Messages.GAME_OVER
// Formatted board output
Messages.GAME_WINNER if the game has a winner
```

Command Implementation Details
----------------------
The game will respond to the following commands during a gameplay

| Name   | Command | Description               |
|--------|---------|---------------------------|
| Play   | X Y     | Put a symbol on the board |  
| Rewind | <<N     | Rewind N turns back       |
| Quit   | :q      | Quit the game             |

If any other command is entered (or the command is not valid), the current turn is repeated.  

### Play command
Puts the symbol associated with the current player at the cell with the entered coordinates.
The following conditions apply for this command

- Two space-delimited numbers
  - otherwise ``Messages.ERROR_INVALID_COMMAND`` is printed to stdout
- Both numbers must be in ``<0, n)`` where ``n`` is the size of the board
  - otherwise ``Messages.ERROR_ILLEGAL_PLAY`` is printed to stdout
- Coordinates are referencing an empty cell
  - otherwise ``Messages.ERROR_ILLEGAL_PLAY`` is printed to stdout
- After the command is executed successfully, next turns starts with the next player

### Rewind command
Allows the current user to rewind back last ``N`` successful play commands (essentially removes last ``N`` placed symbol).
The following conditions apply for this command

- Format is ``<<N`` where N is a number
  - otherwise ``Messages.ERROR_INVALID_COMMAND`` is printed to stdout
- N must be in ``<0, h)`` where ``h`` is the size of history
  - otherwise ``Messages.ERROR_REWIND`` is printed to stdout
- After the command is executed successfully, next turns starts with the next player

Be aware that this command is tight to the history size specified when starting the game.

- For games with a history size of 3 it is possible to rewind at most 2 turns back
- For games with a history size of 1 it is not possible to rewind at all

### Quit command
This commands stops and exits the game
The following conditions apply fort this command

- Format is ``:q``
- Execution of this command does not count as a turn 
  - See the example at the end of this README 
- After the command is executed successfully the program exits


### Project structure
The structure of the meet the following criteria.

1. Package ```cz.muni.fi.pb162.hw01``` contains classes and interfaces provided as a part of the assignment.
2. Package  ```cz.muni.fi.pb162.hw01.impl``` contain implementation.


### Compiling the project
The project can be compiled and packaged in this way:

```bash
$ mvn clean install
```

The checks for missing documentation and style violation will produce an error.
You can temporarily disable this behavior when running this command.

```bash
$ mvn clean install -Dcheckstyle.fail=false
```


### Running the application
The build descriptor is configured to produce a single runnable jar file located at `target/application.jar`. The application can be run using the following command

```
# Start a game on 3x3 board, requiring 3 symbols in a line to win and the ability to rewind 2 turns back
$ java -jar application.jar -s 3 -w 3 --players xo -h 3                                      
Turn: 1
-------
| | | |
-------
| | | |
-------
| | | |
-------
Enter your turn (x): 0 0
===

Turn: 2
-------
|x| | |
-------
| | | |
-------
| | | |
-------
Enter your turn (o): 1 2
===

Turn: 3
-------
|x| | |
-------
| | |o|
-------
| | | |
-------
Enter your turn (x): 2 0
===

Turn: 4
-------
|x| | |
-------
| | |o|
-------
|x| | |
-------
Enter your turn (o): <<2
===

Turn: 5
-------
|x| | |
-------
| | | |
-------
| | | |
-------
Enter your turn (x): :q
===

Game over after 4 turns
-------
|x| | |
-------
| | | |
-------
| | | |
-------
```
