package Rolit;

import java.util.Scanner;

import commands.Command;
import logic.Board;
import logic.Color;
import logic.Game;
import utils.StringUtils;
import view.GamePrinter;

public class Controller {
	private Scanner input;
	private Game game;
	private GamePrinter printer;
	private static final String PROMPT = "Command > ";
	private static final String NAME_PLAYERS = "Name the players: ";
	private static final String INITIAL_MESSAGE = "Choose an option:";
	private static final String CHOOSE_COLOR = "Choose a color shortcut: ";
	private static final String NUMBER_PLAYERS_MSG = "Choose the number of players [2 - " + Color.size() +"]";
	private static final String ERROR_PLAYERS_MSG = "Number of players must be a number between 2 and " + Color.size() + " (inclusive)";
	private static final String BOARD_MSG = "Choose your board size [8 - "+ Board.MAX_SIZE + "]";
	private static final String BOARD_ERROR = "Board size must be a number between 8 and 15 (inclusive)";
	private static final String LOAD_MSG = "Type the name of the file (. to load default file): ";

	private final String NEW_GAME = "New game";
	private final String LOAD_GAME = "Load game";
	private final String INVALID_OPTION = "Invalid option. Try again.";

	private final String[] optionsArray = { NEW_GAME, LOAD_GAME };

	public Controller() {
		input = new Scanner(System.in);
	}

	private void printGame() {
		System.out.println(this.printer);
	}
	
	private void printTurn() {
		System.out.println("Turn: " + game.getCurrentPlayer().getName() + " (" + game.getCurrentColor() + ")");
	}
	
	private int menu() {
		System.out.println();
		System.out.println(INITIAL_MESSAGE);
		System.out.println();

		for (int i = 0; i < optionsArray.length; ++i)
			System.out.println((i + 1) + ". " + optionsArray[i]);

		int respuesta = 1;
		boolean repeat = true;

		while (repeat) {
			respuesta = this.input.nextInt();
			if (respuesta - 1 >= 0 && respuesta - 1 < optionsArray.length)
				repeat = false;
			else
				System.out.println(INVALID_OPTION);
		}
		return respuesta;
	}

	private void play() {
		boolean refreshDisplay = true;
		input.nextLine();
		while (!game.isFinished()) {
			if (refreshDisplay) {
				printTurn();
				printGame();
			}
			System.out.print(PROMPT);
			String s = input.nextLine();
			String[] parameters = s.toLowerCase().trim().split(" ");
			Command command = null;
			try {
				command = Command.getCommand(parameters);
				refreshDisplay = command.execute(game);
			} catch (Exception e) {
				System.out.println(e.getMessage());
				System.out.println();
			}
		}

		if (refreshDisplay)
			printGame();

		if (!game.exited())
			System.out.println(this.printer.showRanking());
	}

	private void chooseColor(int numPlayers) {
		System.out.println(NAME_PLAYERS);
		System.out.println();
		for (int i = 0; i < numPlayers; ++i) {
			boolean added = false;

			System.out.print("Player " + (i + 1) + ": ");
			input.nextLine();
			String name = input.nextLine();
			 while (!added) {
				try {
					System.out.println(game.availableColors());
					System.out.print(CHOOSE_COLOR);
					char c = this.input.next().charAt(0); // HAGO QUE SEA UN STRING POR SI EL USUARIO INTRODUCE MAS DE UN CARACTER
					game.tryToAddPlayer(name, c);
					added = true;
				} catch (IllegalArgumentException e) {
					System.out.println(e.getMessage());
					added = false;
				}
			}
		}
	}

	private int numPlayers() {
		System.out.println(NUMBER_PLAYERS_MSG);
		int nPlayers = this.input.nextInt();
		while (nPlayers < 2 || nPlayers > Color.size()) {
			System.out.println(ERROR_PLAYERS_MSG);
			nPlayers = this.input.nextInt();
		}
		return nPlayers;
	}
	
	private Board chooseBoard() {
		System.out.println(BOARD_MSG);
		int board_size = this.input.nextInt();
		while (board_size < 8 || board_size > Board.MAX_SIZE) {
			System.out.println(BOARD_ERROR);
			board_size = this.input.nextInt();
		}
		return new Board(board_size);
	}
	
	private void createGame() {
		this.game = new Game(this.chooseBoard());
		this.chooseColor(this.numPlayers());
	}
	
	private void createPrinter() {
		this.printer = new GamePrinter(game);
	}
	
	public void run() {
		int option = this.menu();
		if (LOAD_GAME.equals(optionsArray[option - 1])) {
			System.out.print(LOAD_MSG);
			String fileName = input.next();
			if(".".equals(fileName))
				game = SaveLoadManager.loadGame();
			else
				game = SaveLoadManager.loadGame(fileName);
		}
		else 
			this.createGame();
		this.createPrinter();
		this.play();
	}
	
}