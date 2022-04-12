package control;

import java.util.Scanner;

import javax.swing.SwingUtilities;

import org.json.JSONObject;

import Builders.GameBuilder;
import commands.Command;
import logic.Game;
import replay.Replay;
import view.GUIView.GamePrinter;
import view.GUIView.MainWindow;

public class ControllerConsole {
	private Game game;
	private Replay replay;
	
	public ControllerConsole() {
		replay = new Replay();
	}
	
	private void printGame() {
		System.out.println(this.game.toString());
	}
	
	private void run() {
		new Thread();
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

	private int mode() {
		
		System.out.println(CHOOSE_MODE);
		
		for (int i = 0; i < modes.length; ++i)
			System.out.println(i + 1 + ". " + modes[i]);

		int respuesta = 1;
		boolean repeat = true;

		while (repeat) {
			respuesta = this.input.nextInt();
			if (respuesta - 1 >= 0 && respuesta - 1 < modes.length)
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
			if (refreshDisplay) 
				this.printGame();
			System.out.print(PROMPT);
			String s = input.nextLine();
			String[] parameters = s.toLowerCase().trim().split(" ");
			Command command = null;
			try {
				command = Command.getCommand(parameters);
				refreshDisplay = command.execute(game);
				replay.addState(s, game.getState());
			} catch (Exception e) {
				System.out.println(e.getMessage());
				System.out.println();
			}
		}

		if (refreshDisplay)
			this.printGame();

		if (!this.game.exited())
			System.out.println(this.game.showRanking());
		
		if(askSaveReplay())
			SaveLoadManager.saveReplay(replay);
		
	}
	
	// FIXME tiene que haber una forma mejor de hacerlo todo
	public void run() {
		int selectedMode = mode();
		
		if (GUI_MODE.equals(modes[selectedMode-1])) {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					//;
					MainWindow mainWindow = new MainWindow();
				}
			});
		}
		else if (CONSOLE_MODE.equals(modes[selectedMode-1])){
			
			boolean repeatMenu;
			do {
				repeatMenu = false;
				int option = this.menu();
				if (NEW_GAME.equals(optionsArray[option - 1]))
					game = GameBuilder.createGame();
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
	}
	
	public void createGame(JSONObject jGame) {
		this.game = GameBuilder.createGame(jGame);
	}
	
	private boolean askSaveReplay() {
		System.out.println(REPLAY_MSG);
		String ans = input.nextLine();
		return "y".equals(ans.toLowerCase());
	}
}
