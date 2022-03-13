package commands;

import Rolit.SaveLoadManager;
import logic.Game;

public class SaveCommand extends Command {
	
	private static final String NAME = "save";

	private static final String DETAILS = "[s]ave";

	private static final String SHORTCUT = "s";

	private static final String HELP = "save the game";
	
	private String filename;
	
	public SaveCommand() {
		super(NAME, DETAILS, SHORTCUT, HELP);
		filename = null;
	}

	@Override
	public boolean execute(Game game) {
		if(filename == null) SaveLoadManager.saveGame(game);
		else SaveLoadManager.saveGame(game, filename);
		return false;
	}
	
	@Override
	public Command parse(String[] words) {
		if(matchCommandName(words[0])) {
			if(words.length == 1) {} //Nada que hacer aqui, filename mantiene null como valor
			else if(words.length == 2) {
				filename = words[1];
			}
			else
				throw new IllegalArgumentException("SaveCommand format can be either \"s\" or \"s\" \"filename\"");
			return this;
		}
		return null;
	}
}
