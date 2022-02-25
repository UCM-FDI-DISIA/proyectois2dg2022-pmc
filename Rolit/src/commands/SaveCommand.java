package commands;

import Rolit.SaveLoadManager;
import logic.Game;

public class SaveCommand extends Command {
	
	private static final String NAME = "save";

	private static final String DETAILS = "[s]ave";

	private static final String SHORTCUT = "s";

	private static final String HELP = "save the game";
	
	public SaveCommand() {
		super(NAME, DETAILS, SHORTCUT, HELP);
	}

	@Override
	public boolean execute(Game game) {
		SaveLoadManager.saveGame();
		return false;
	}
}
