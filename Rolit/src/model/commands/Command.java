package model.commands;

import org.json.JSONObject;

import model.logic.Game;
import model.logic.Replayable;

public abstract class Command implements Replayable {	
	private static final String UNKNOWN_COMMAND_MSG = "Unknown command. Type \"help\" to see the available commands.";
	protected static final String INCORRECT_NUMBER_OF_ARGS_MSG = "Incorrect number of arguments";	
	protected static final String PARSE_LONG_MSG = "the seed is not a proper long number";	
	private final String name;
	private final String shortcut;
	private final String details;
	private final String help;
	
	protected static final Command[] AVAILABLE_COMMANDS = {
			new ExitCommand(),
			new HelpCommand(),
			new PlaceCubeCommand(),
			new SaveCommand()
	};

	public Command(String name, String details, String shortcut, String help) {
		this.name = name;
		this.details = details;
		this.shortcut = shortcut;
		this.help = help;
	}	
	
	public static Command getCommand(String[] commandWords) { //Habria que hacer un bucle para ver con qu� comando coincide
		Command command = null;
		boolean found = false;
		int i = 0;
		while(!found && i < AVAILABLE_COMMANDS.length) {
			command = AVAILABLE_COMMANDS[i].parse(commandWords);
			if(command != null) found = true;
			else i++;
		}
		if(!found) {
			throw new IllegalArgumentException(String.format("[ERROR]: %s", UNKNOWN_COMMAND_MSG));
		}
		return command;
	}
	
	public abstract void execute(Game game) throws Exception;

	protected boolean matchCommandName(String name) {
		return this.shortcut.equalsIgnoreCase(name) || this.name.equalsIgnoreCase(name);
	}
	
	protected Command parse(String[] words)  {
		if (matchCommandName(words[0])) {
			if (words.length != 1) {
				throw new IllegalArgumentException(String.format("[ERROR]: Command %s: %s", name, INCORRECT_NUMBER_OF_ARGS_MSG));
			} else {
				return this;
			}
		}
		return null;
	}
	
	protected String helpMsg() {
		StringBuilder msg = new StringBuilder(this.details);
		msg.append(": ").append(this.help);
		return msg.toString();
	}
	
	public String infoForHelp() {
		return details + ": " + help;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	@Override
	public JSONObject report() {
		return null;
	}
	
}
