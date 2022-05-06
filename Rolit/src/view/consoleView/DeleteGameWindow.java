package view.consoleView;

import java.util.List;

import model.SaveLoadManager;

public class DeleteGameWindow implements ConsoleWindow {
	private static final String CHOOSE_NUMBER_MSG = "Choose a number";
	private static final String INVALID_OPTION = "Invalid option. Try again.";
	private static final int GO_BACK_INT = -1;
	private static final String HOW_TO_EXIT_MSG = " (" + GO_BACK_INT + " to go back)";

	@Override
	public boolean open() {
		this.clear();
		List<String> savedGames = SaveLoadManager.getListOfSavedGames();
		// En caso de que todo haya ido bien
		for (int i = 0; i < savedGames.size(); i++)
			System.out.println(String.format("%d. %s", i + 1, savedGames.get(i)));
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
		return true;
	}

	@Override
	public Object get() {
		return null;
	}
}
