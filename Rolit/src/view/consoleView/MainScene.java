package view.consoleView;

import java.util.Scanner;

import Builders.GameBuilder;
import control.Controller;
import control.SaveLoadManager;

public class MainScene {
	private static final String INITIAL_MESSAGE = "Choose an option:";
	private static final String OPTIONS[] = {"New Game", "Load Game", "Delete Game", "Replay Game"};
	private static final String PROMPT = "Command > ";	
	private static final String LOAD_MSG = "Type the name of the file (. to load default file): ";
	private final int GO_BACK_INT = -1;
	private final String HOW_TO_EXIT_MSG = " (" + GO_BACK_INT + " to go back)";
	private final String NEW_GAME = "New game";
	private final String LOAD_GAME = "Load game";
	private final String DELETE_GAME = "Delete a game";
	private final String LIST_MSG = "List of saved games: ";
	private final String CHOOSE_NUMBER_MSG = "Choose a number";
	private final String INVALID_OPTION = "Invalid option. Try again.";
	private final String REPLAY_GAME = "Replay game";
	private final String REPLAY_MSG = "Do you want to save the replay of the game? (y/n)";	
	private static final String CHOOSE_MODE = "Choose mode: ";
	private static final String CONSOLE_MODE = "Console Mode";
	private static final String GUI_MODE = "GUI Mode";
	private Scanner input = new Scanner(System.in);
	private Controller ctr;
	
	public MainScene(Controller ctr) {
		this.ctr = ctr;
		boolean repeatMenu;
		do {
			repeatMenu = false;
			int option = this.menu();
			if (NEW_GAME.equals(OPTIONS[option - 1]))
				// genGame() va a devolver un JSONObject
				ctr.createGame(this.genGame());
				// game = GameBuilder.createGame();
			else if (LOAD_GAME.equals(optionsArray[option - 1])) {
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
							System.out.print(CHOOSE_NUMBER_MSG + HOW_TO_EXIT_MSG + " :");
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
				playMode = false;
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
				playMode = false;
				//System.out.print("Escribe el nombre del replay a cargar .txt (temporal):");
				//input.nextLine();
				//String loadFile = input.nextLine();
				replay = SaveLoadManager.loadReplay();
				replay.startReplay();
			}			
		} while (repeatMenu);
		if(playMode) {
			this.play();
		}
		
	}
	
	private int menu() {
		int option;
		System.out.println(INITIAL_MESSAGE);
		for (int i = 0; i < OPTIONS.length; i++)
			System.out.println(String.format("%s %d", OPTIONS[i], i + 1));
		option = input.nextInt();
		boolean repeat = true;
		while (repeat) {
			option = this.input.nextInt();
			if (option - 1 >= 0 && option - 1 < OPTIONS.length)
				repeat = false;
			else
				System.out.println(INVALID_OPTION);
		}
		return option;
	}
	
	

}
