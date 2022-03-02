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
import logic.Color;
import logic.Game;
import view.GamePrinter;

public class Controller {
	private Scanner input;
	private Game game;
	private GamePrinter printer;
	private static final String PROMPT = "Command > ";
	private static final int numberOfPlayers = 4;
	private static final String NAME_PLAYERS = "Name the players: ";
	private static final String INITIAL_MESSAGE = "Choose an option:";
	private static final String CHOOSE_COLOR = "Choose a color shortcut:";

	private final String NEW_GAME = "New game";
	private final String LOAD_GAME = "Load game";
	private final String INVALID_OPTION = "Invalid option. Try again.";

	private final String[] optionsArray = { NEW_GAME, LOAD_GAME };

	public Controller(Game game) {
		this.game = game;
		this.printer = new GamePrinter(game);
		input = new Scanner(System.in);
	}

	private void printGame() {
		System.out.println(this.printer);
	}

	public void run() {
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

	private static List<String> chooseColor(Scanner scanner) {
		System.out.println(NAME_PLAYERS);
		System.out.println();
		List<String> players = new ArrayList<String>();

		for (int i = 0; i < numberOfPlayers; ++i) {
			boolean added = false;

			System.out.print("Player " + (i + 1) + ": ");
			String name = scanner.nextLine();

			 while (!added) {
				try {
					System.out.println(game.availableColors());
					System.out.print(CHOOSE_COLOR);
					String c = scanner.next(); // HAGO QUE SEA UN STRING POR SI EL USUARIO INTRODUCE MAS DE UN CARACTER
					game.tryToAddPlayer(name, c);
					added = true;
				} catch (IllegalArgumentException e) {
					System.out.println(e.getMessage());
					added = false;
				}
			}

			players.add(name);

		}

		return players;

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
			respuesta = input.nextInt();
			if (respuesta - 1 >= 0 && respuesta - 1 < optionsArray.length)
				repeat = false;
			else
				System.out.println(INVALID_OPTION);
		}

		if (LOAD_GAME.equals(optionsArray[respuesta - 1])) {
			SaveLoadManager.loadGame(game);
		} else {
			chooseColor(input);
		}

		run();

		return 0;
	}
}