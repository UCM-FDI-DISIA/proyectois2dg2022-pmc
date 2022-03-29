package commands;

import logic.Game;

public class HelpCommand extends Command {	
	private static final String NAME = "help";
	private static final String DETAILS = "[h]elp";
	private static final String SHORTCUT = "h";
	private static final String HELP = "show this help";
	
	public HelpCommand() {
		super(NAME, DETAILS, SHORTCUT, HELP);
	}

	@Override
	public boolean execute(Game game) {
		StringBuilder buffer = new StringBuilder("Available commands:");
		for(Command c : AVAILABLE_COMMANDS) {
			buffer.append(String.format("%n%s", c.infoForHelp()));
		}
		System.out.println(buffer.toString());
		return false;
	}
}
