package view.consoleView;

import java.util.List;

import model.SaveLoadManager;
import model.replay.Replay;

/**
 * This class displays the necessary information in order to replay a saved replay, it also
 * gathers users intentions
 * It implements ConsoleWindow
 * @author PMC
 *
 */
public class LoadReplayWindow implements ConsoleWindow {
	private static final String LIST_MSG = "List of saved replays: ";
	private static final String CHOOSE_NUMBER_MSG = "Choose a number";
	private static final int GO_BACK_INT = -1;
	private static final String HOW_TO_EXIT_MSG = " (" + GO_BACK_INT + " to go back)";
	private static final String INVALID_OPTION = "Invalid option. Try again.";
	private Replay replay;

	@Override
	public boolean open() {
		this.clear();
		System.out.println(LIST_MSG);
		List<String> savedGames = null;
		savedGames = SaveLoadManager.getListOfSavedReplays();
		// In case everything has gone well
		for (int i = 0; i < savedGames.size(); i++)
			System.out.println(String.format("%d. %s", i + 1, savedGames.get(i)));
		boolean repeatChooseNumber;
		do {
			repeatChooseNumber = false;
			System.out.print(CHOOSE_NUMBER_MSG + HOW_TO_EXIT_MSG + " :");
			try {
				int numberOfSavedGameInt = Integer.valueOf(input.next());
				if (numberOfSavedGameInt != GO_BACK_INT)
					this.replay = SaveLoadManager.loadReplay(numberOfSavedGameInt);
				else {
					this.replay = null;
					return true;
				}
			} catch (Exception e) {
				System.out.println(INVALID_OPTION);
				repeatChooseNumber = true;
			}
		} while (repeatChooseNumber);
		return false;

	}

	@Override
	public Object get() {
		return this.replay;
	}
}
