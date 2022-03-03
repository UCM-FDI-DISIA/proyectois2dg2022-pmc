package Rolit;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import commands.Command;
import logic.Board;
import logic.Color;
import logic.Game;
import view.GamePrinter;

public class Controller {
	private Scanner input;
	private Game game;
	private GamePrinter printer;
	private static final String PROMPT = "Command > ";
	private static final String NAME_PLAYERS = "Name the players: ";
	private static final String INITIAL_MESSAGE = "Choose an option:";
	private static final String CHOOSE_COLOR = "Choose a color shortcut:";
	private static final String NUMBER_PLAYERS_MSG = "How many players do you want?";
	private static final String ERROR_PLAYERS_MSG = "Number of players must be a number between 2-10";
	private static final String BOARD_MSG = "Choose your board size (the lenght of the side), it must be more than 8";
	private static final String BOARD_ERROR = "Board size must be a number between 8-15";
	
	private final String NEW_GAME = "New game";
	private final String LOAD_GAME = "Load game";
	private final String INVALID_OPTION = "Invalid option. Try again.";

	private final String[] optionsArray = { NEW_GAME, LOAD_GAME };

	public Controller() {
		this.printer = new GamePrinter(game);
		input = new Scanner(System.in);
	}

	private void printGame() {
		System.out.println(this.printer);
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
		while (!game.isFinished()) {
			if (refreshDisplay)
				printGame();
			System.out.println(PROMPT);
			String s = input.nextLine();
			String[] parameters = s.toLowerCase().trim().split(" ");
			Command command = null;
			try {
				command = Command.getCommand(parameters);
				refreshDisplay = command.execute(game);
			} catch (Exception e) {
				System.out.println(e.getMessage());
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
			String name = this.input.nextLine();

			 while (!added) {
				try {
					System.out.println(game.availableColors());
					System.out.print(CHOOSE_COLOR);
					String c = this.input.next(); // HAGO QUE SEA UN STRING POR SI EL USUARIO INTRODUCE MAS DE UN CARACTER
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
	
	public void run() {
		int option = this.menu();
		if (LOAD_GAME.equals(optionsArray[option - 1]))
			SaveLoadManager.loadGame(game);
		else 
			this.createGame();
		this.play();
	}
	
}