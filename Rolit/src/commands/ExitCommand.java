package commands;

import logic.GameClassic;

public class ExitCommand extends Command {
	private static final String NAME = "exit";

	private static final String DETAILS = "[e]xit";

	private static final String SHORTCUT = "e";

	private static final String HELP = "exit game";

	public ExitCommand() {
		super(NAME, DETAILS, SHORTCUT, HELP);
	}
	
	@Override
	public boolean execute(GameClassic game) {
		game.setExit();
		 System.out.println("See ya!");
		return false;
	}
}
