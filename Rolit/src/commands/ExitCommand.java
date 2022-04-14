package commands;

import logic.Game;

public class ExitCommand extends Command {
	private static final String NAME = "exit";
	private static final String DETAILS = "[e]xit";
	private static final String SHORTCUT = "e";
	private static final String HELP = "exit game";

	public ExitCommand() {
		super(NAME, DETAILS, SHORTCUT, HELP);
	}
	
	@Override
	public void execute(Game game) throws Exception {
		game.setExit();
	}
}
