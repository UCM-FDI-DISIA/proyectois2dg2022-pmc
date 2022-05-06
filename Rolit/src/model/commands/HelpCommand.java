package model.commands;

import model.logic.Game;
import model.rolitexceptions.HelpException;

public class HelpCommand extends Command {	
	private static final String NAME = "help";
	private static final String DETAILS = "[h]elp";
	private static final String SHORTCUT = "h";
	private static final String HELP = "show this help";
	
	public HelpCommand() {
		super(NAME, DETAILS, SHORTCUT, HELP);
	}

	@Override
	public void execute(Game game) throws Exception {
		StringBuilder buffer = new StringBuilder("Available commands:");
		for(Command c : AVAILABLE_COMMANDS) {
			buffer.append(String.format("%n%s", c.infoForHelp()));
		}
		throw new HelpException(buffer.toString());		
	}
}
