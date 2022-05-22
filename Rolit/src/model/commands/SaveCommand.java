package model.commands;

import model.SaveLoadManager;
import model.logic.Game;

/**
 * This class represents a command to save the game
 * @author PMC
 *
 */
public class SaveCommand extends Command {	
	private static final String NAME = "save";
	private static final String DETAILS = "[s]ave";
	private static final String SHORTCUT = "s";
	private static final String HELP = "save the game";
	private static final String SUCCESS_SAVE_MSG = "Game saved successfully";
	private static final String ERROR_SAVE_MSG = "Failed to save the file";
	private String filename;
	
	/**
	 * Constructor
	 */
	public SaveCommand() {
		super(NAME, DETAILS, SHORTCUT, HELP);
		filename = null;
	}
	
	/**
	 * Constructor
	 * @param filename the name of the file in which the game is saved
	 */
	public SaveCommand(String filename) {
		this();
		this.filename = filename;
	}

	@Override
	public void execute(Game game) throws Exception {
		boolean success = true;
		if(filename == null) success = SaveLoadManager.saveGame(game);
		else success = SaveLoadManager.saveGame(game, filename);
		if (success) {
			System.out.println(SUCCESS_SAVE_MSG);
			game.onStatusChange(SUCCESS_SAVE_MSG);
		}
		else {
			System.out.println(ERROR_SAVE_MSG);
			game.onStatusChange(SUCCESS_SAVE_MSG);			
		}
	}
	
	@Override
	public Command parse(String[] words) {
		if(matchCommandName(words[0])) {
			if(words.length == 1) {} //Nothing to do here, filename maintains null as value
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
