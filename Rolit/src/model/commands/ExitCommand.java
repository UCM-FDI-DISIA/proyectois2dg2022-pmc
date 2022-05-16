package model.commands;

import model.logic.Game;

/**
 * This class represents a command to exit the game
 * @author PMC
 *
 */
public class ExitCommand extends Command {
	private static final String NAME = "exit";
	private static final String DETAILS = "[e]xit";
	private static final String SHORTCUT = "e";
	private static final String HELP = "exit game";

	/**
	 * Constructor
	 */
	public ExitCommand() {
		super(NAME, DETAILS, SHORTCUT, HELP);
	}
	
	@Override
	public void execute(Game game) throws Exception {
		game.exit();
	}
}
