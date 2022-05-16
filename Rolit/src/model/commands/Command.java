package model.commands;

import org.json.JSONObject;

import model.logic.Game;
import model.logic.Replayable;

/**
 * This class represents a command for the Rolit game
 * @author PMC
 *
 */
public abstract class Command implements Replayable {	
	
	/**
	 * Message shown when the command is unknown
	 */
	private static final String UNKNOWN_COMMAND_MSG = "Unknown command. Type \"help\" to see the available commands.";
	
	/**
	 * Message shown when the number of arguments given is not correct
	 */
	protected static final String INCORRECT_NUMBER_OF_ARGS_MSG = "Incorrect number of arguments";	
	
	/**
	 * Message shown when the seed is not a proper long number
	 */
	protected static final String PARSE_LONG_MSG = "the seed is not a proper long number";	
	
	/**
	 * Name of the command
	 */
	private final String name;
	
	/**
	 * First letter of the command
	 */
	private final String shortcut;
	
	/**
	 * The name of the command with its shortcut specified in it
	 */
	private final String details;
	
	/**
	 * Phrase which explains what the command does
	 */
	private final String help;
	
	/**
	 * Array of every available Command
	 */
	protected static final Command[] AVAILABLE_COMMANDS = {
			new ExitCommand(),
			new HelpCommand(),
			new PlaceCubeCommand(),
			new SaveCommand()
	};

	/**
	 * Constructor
	 * @param name the name of the command
	 * @param details the name of the command with its shortcut specified in it
	 * @param shortcut the first letter of the name
	 * @param help a phrase which explains what the command does
	 */
	public Command(String name, String details, String shortcut, String help) {
		this.name = name;
		this.details = details;
		this.shortcut = shortcut;
		this.help = help;
	}	
	
	/**
	 * Method that gets a command given its information
	 * @param commandWords information of the command
	 * @throws IllegalArgument Exception thrown if the command given is unknown
	 * @return Command found
	 */
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
	
	/**
	 * This method manages the execution of the command
	 * @param game the game in which the command is used
	 * @throws Exception thrown if an error occurs during the execution of the command
	 */
	public abstract void execute(Game game) throws Exception;

	/**
	 * This method verifies if the name of a command given matches a real command name or its shortcut
	 * @param name name of the command given
	 * @return true if the name of the command given matches the real command name or its shortcut
	 */
	protected boolean matchCommandName(String name) {
		return this.shortcut.equalsIgnoreCase(name) || this.name.equalsIgnoreCase(name);
	}
	
	/**
	 * Method that parses the information of a command
	 * @param words the information to parse
	 * @throws IllegalArgumentException thrown if the number of arguments given is not correct
	 * @return Command parsed if the information is correct or null if it is not
	 */
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
	
	/**
	 * Method that creates a help message from the details of the command
	 * @return String the message which contains the help information
	 */
	protected String helpMsg() {
		StringBuilder msg = new StringBuilder(this.details);
		msg.append(": ").append(this.help);
		return msg.toString();
	}
	
	/**
	 * Method that shows the details and help of the command
	 * @return String the details of the command plus its help information
	 */
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
