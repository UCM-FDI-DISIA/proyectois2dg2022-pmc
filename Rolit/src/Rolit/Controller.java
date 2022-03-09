package Rolit;

import java.util.Scanner;

import commands.Command;
import logic.Game;
import view.GamePrinter;

public class Controller {
	private Scanner input;
	private Game game;
	private GamePrinter printer;
	private static final String PROMPT = "Command > ";
	private static final String INITIAL_MESSAGE = "Choose an option:";
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
			game = GameGenerator.createGame();
		this.createPrinter();
		this.play();
	}
	
}