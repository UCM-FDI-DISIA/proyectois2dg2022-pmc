package view.consoleView;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import control.SaveLoadManager;

public class DeleteGameWindow implements ConsoleWindow{
	private static final String CHOOSE_NUMBER_MSG = "Choose a number";
	private static final String INVALID_OPTION = "Invalid option. Try again.";
	private static final int GO_BACK_INT = -1;
	private static final String HOW_TO_EXIT_MSG = " (" + GO_BACK_INT + " to go back)";
	
	@Override
	public boolean open() {
		this.clear();
		try {
			List<String> savedGames = SaveLoadManager.showSavedGames();
			// En caso de que todo haya ido bien
			if (!savedGames.isEmpty()) {
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
		catch(IOException e) {
			System.out.println(e.getMessage());
		}
		return true;
	}

	@Override
	public Object get() {
		// TODO Auto-generated method stub
		return null;
	}
}
