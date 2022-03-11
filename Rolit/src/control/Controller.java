package control;

import java.util.Scanner;

import Rolit.GameGenerator;
import commands.Command;
import logic.Game;
import replay.Replay;
import view.GamePrinter;

public class Controller {
	private Scanner input;
	private Game game;
	private GamePrinter printer;
	private Replay replay;
	private static final String PROMPT = "Command > ";
	private static final String INITIAL_MESSAGE = "Choose an option:";
	private static final String LOAD_MSG = "Type the name of the file (. to load default file): ";
	private final int GO_BACK_INT = -1;
	private final String HOW_TO_EXIT_MSG = " (" + GO_BACK_INT + " to go back)";
	private final String NEW_GAME = "New game";
	private final String LOAD_GAME = "Load game";
	private final String DELETE_GAME = "Delete a game";
	private final String LIST_MSG = "List of saved games: ";
	private final String CHOOSE_NUMBER_MSG = "Choose a number: ";
	private final String INVALID_OPTION = "Invalid option. Try again.";
	private static final String REPLAY_FILE_NAME = "replay.txt";
	private final String REPLAY_GAME = "Replay game";
	private final String[] optionsArray = { NEW_GAME, LOAD_GAME, DELETE_GAME, REPLAY_GAME};

	public Controller() {
		input = new Scanner(System.in);
		replay = new Replay();
	}

	private void printGame() {
		System.out.println(this.printer);
	}
	
	private void printTurn() {
		System.out.println("Turn: " + game.getCurrentPlayer());
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
	
	// FIXME tiene que haber una forma mejor de hacerlo todo
	public void run() {
		boolean repeatMenu;
		
		do {
			
			repeatMenu = false;
			
			int option = this.menu();
			
			if (NEW_GAME.equals(optionsArray[option - 1]))
				game = GameGenerator.createGame();
			
			else if (LOAD_GAME.equals(optionsArray[option - 1]))
			{
					System.out.println(LIST_MSG);
					boolean opened = SaveLoadManager.showSavedGames();
					
					if (!opened) {
						System.out.println(LOAD_MSG);
						String fileName = input.next();
						
						if(".".equals(fileName)) {
							game = SaveLoadManager.loadGame();
						}
						else
							game = SaveLoadManager.loadGame(fileName);
						
					}
					else {
						boolean repeatChooseNumber;
						
						do {
							
							repeatChooseNumber = false;
							System.out.print(CHOOSE_NUMBER_MSG + HOW_TO_EXIT_MSG);
							
							try {
								
								int numberOfSavedGameInt = Integer.valueOf(input.next());
								
								if (numberOfSavedGameInt != GO_BACK_INT)
									game = SaveLoadManager.loadGame(numberOfSavedGameInt);
								else
									repeatMenu = true;
								
							} catch (Exception e) {
								
								System.out.println(INVALID_OPTION);
								repeatChooseNumber = true;
								
							}
							
						} while (repeatChooseNumber);
						
					}
						
			}
			
			else if (DELETE_GAME.equals(optionsArray[option - 1])) {
				
				repeatMenu = true;
				
				boolean opened = SaveLoadManager.showSavedGames();
				
				if (opened) {
					
					boolean repeatChooseNumber;
					
					do {
						
						repeatChooseNumber = false;
						System.out.print(CHOOSE_NUMBER_MSG + HOW_TO_EXIT_MSG);
						
						try {
							int numberOfSavedGameInt = Integer.valueOf(input.next());
							if (numberOfSavedGameInt != GO_BACK_INT)
								SaveLoadManager.removeGame(numberOfSavedGameInt);
							
						} catch (Exception e) {
							
							System.out.println(INVALID_OPTION);
							repeatChooseNumber = true;
							
						}
						
					} while (repeatChooseNumber);
					
				}
				
			}
			else if(REPLAY_GAME.equals(optionsArray[option - 1])) {
				System.out.print("Escribe el nombre del replay a cargar .txt (temporal)");
				replay = SaveLoadManager.loadReplay();
			}			
		} while (repeatMenu);
		this.createPrinter();
		this.play();
	}
	
}