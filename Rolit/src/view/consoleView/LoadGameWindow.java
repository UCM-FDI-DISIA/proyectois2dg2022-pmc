package view.consoleView;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import org.json.JSONObject;

import control.SaveLoadManager;

public class LoadGameWindow implements ConsoleWindow {
	private static final String LIST_MSG = "List of saved games: ";
	private static final String LOAD_MSG = "Type the name of the file (. to load default file): ";
	private static final String CHOOSE_NUMBER_MSG = "Choose a number";
	private static final int GO_BACK_INT = -1;
	private static final String HOW_TO_EXIT_MSG = " (" + GO_BACK_INT + " to go back)";
	private static final String INVALID_OPTION = "Invalid option. Try again.";
	private JSONObject json;
	
	@Override
	public boolean open() {
		this.clear();
		System.out.println(LIST_MSG);
		List<String> savedGames;
		try {
			savedGames = SaveLoadManager.showSavedGames();
			// En caso de que no haya habido problemas
			for (int i = 0; i < savedGames.size(); i++)
				System.out.println(String.format("%d. %s", i, savedGames.get(i)));			
			System.out.println(LOAD_MSG);
			String fileName = input.next();
			if(".".equals(fileName))
				this.json = SaveLoadManager.loadGame();
			else
				this.json = SaveLoadManager.loadGame(fileName);	
			return false;
		}
		catch (IOException ioe){
			boolean repeatChooseNumber;
			do {
				repeatChooseNumber = false;
				System.out.print(CHOOSE_NUMBER_MSG + HOW_TO_EXIT_MSG + " :");
				try {
					int numberOfSavedGameInt = Integer.valueOf(input.next());
					if (numberOfSavedGameInt != GO_BACK_INT)
						this.json = SaveLoadManager.loadGame(numberOfSavedGameInt);
					else {
						this.json = null;
						return true;
					}						
				} catch (Exception e) {
					System.out.println(INVALID_OPTION);
					repeatChooseNumber = true;
				}
			} while (repeatChooseNumber);
		}
		return true;
	}

	@Override
	public Object get() {
		return this.json;
	}
}
