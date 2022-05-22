package view.consoleView;

import org.json.JSONArray;
import org.json.JSONObject;

import model.builders.GameBuilder;
import model.logic.Board;
import model.logic.Color;
import model.logic.Shape;
import model.strategy.Strategy;

/**
 * This class displays the necessary information in order to create a new game, it also
 * gathers users intentions 
 * It implements ConsoleWindow
 * @author PMC
 *
 */
public class NewGameWindow implements ConsoleWindow {
	private static final String NUMBER_PLAYERS_MSG = "Choose the number of players [2 - " + Color.size() +"]";
	private static final String ERROR_MODES_MSG = "Game mode must be on of these:" + GameBuilder.availableModes();
	private static final String ERROR_PLAYERS_MSG = "Number of players must be a number between 2 and " + Color.size() + " (inclusive)";
	private static String AVAILABLE_MODES_MSG = "Choose game mode between " + GameBuilder.availableModes() + " (case sensitive)";
	private static final String BOARD_MSG = "Choose a board shape. ";
	private static final String BOARD_ERROR = "ERROR: Not found such shape. ";
	protected static final String STRATEGY_ERROR= "ERROR: Not a valid strategy. ";
	protected static final String NAME_PLAYERS = "Name the players (if you want a player to be an AI, end the name with AI (i.e. Mark AI): ";
	protected static final String CHOOSE_COLOR = "Choose a color shortcut: ";
	protected static final String CHOOSE_AI_DIFFICULTY_MSG = "Choose the AI difficulty: ";

	private static final String TYPE = "Game";
	private	static NewGameWindow[] everyGameWindows = { new NewGameClassicWindow(), new NewGameTeamsWindow() };
	protected static int nPlayers;	
	protected static JSONObject json;
	
	@Override
	public boolean open() {
		json = new JSONObject();
		//We choose the specific game mode
		System.out.println(AVAILABLE_MODES_MSG);
		String type = input.nextLine();
		while(!GameBuilder.isAvailableMode(type)) {
			System.out.println(ERROR_MODES_MSG);
			type = input.nextLine();
		}
		json.put("type", type);
		//We choose the number of players
		System.out.println(NUMBER_PLAYERS_MSG);
		nPlayers = input.nextInt();
		input.nextLine();
		while (nPlayers < 2 || nPlayers > Color.size()) {
			System.out.println(ERROR_PLAYERS_MSG);
			nPlayers = input.nextInt();
			input.nextLine();
		}
		//We choose the specific board
		System.out.print(BOARD_MSG);
		String board_shape;
		do {
			System.out.print(Shape.availableShapes());
			board_shape = input.nextLine();
			if (Shape.valueOfIgnoreCase(board_shape) == null) {
				System.out.print(BOARD_ERROR);
			}
		} while (Shape.valueOfIgnoreCase(board_shape) == null);		
		json.put("board", new Board(Shape.valueOfIgnoreCase(board_shape)).report());
		this.parse(type).open();
		json.put("turn", json.getJSONArray("players").getJSONObject(0).get("color"));
		return false;
	}
	
	/**
	 * It checks whether the game as a parameter is equal to any of the available game modes
	 * @param type Type of the game
	 * @return The appropriate GameBuilder if the parameter is equal to any of the available modes or null otherwise
	 */
	private NewGameWindow parse(String type) {
		for (NewGameWindow g : everyGameWindows) {
			if (g.match(type))
				return g;
		}
		return null;
	}
	
	/**
	 * It checks whether the game as a parameter is equal to any of the available game modes
	 * @param type Type of the game
	 * @return true If the parameter is equal to any of the available modes or false otherwise
	 */
	protected boolean match(String type) {
		return NewGameWindow.TYPE.equals(type);
	}

	@Override
	public Object get() {
		return NewGameWindow.json;
	}
	
	/**
	 * It validates that the player that we are trying to create has appropriate data
	 * @param players JSONArray that contains the players
	 * @param name Name of the player we are trying to create
	 * @param color Color of the player we are trying to create
	 * @param strat Strategy of the player we are trying to create in case it is an artificial intelligence 
	 * @return JSONObject of the player in case that it has appropriate data, it throws an exception otherwise
	 */
	protected final JSONObject validatePlayer(JSONArray players, String name, Color color, String strat) {
		JSONObject jPlayer = new JSONObject();
		if (color == null)
			 throw new IllegalArgumentException("The shortcut does not match any color");
		for (int j = 0; j < players.length(); j++) {
			if (players.getJSONObject(j).get("color").equals(color.toString()))
			 	 throw new IllegalArgumentException("The selected color is not available");
		}
		Strategy s = null;
		if(strat != null) {
			s = Strategy.parse(color, strat);
		}
		jPlayer.put("name", name);
		jPlayer.put("color", color.toString());
		if(s != null)
			jPlayer.put("strategy", s.toString());
		return jPlayer;
	}
	
	/**
	 * It shows the availableColors, i.e. those that have not been previously selected
	 * @param players JSONArray that contains the players
	 * @return
	 */
	protected final String availableColors(JSONArray players) {
		StringBuilder str = new StringBuilder();
		boolean valid;
		for (Color c : Color.values()) {
			valid = true;
			for (int j = 0; j < players.length() && valid; j++)
				 valid = !players.getJSONObject(j).get("color").equals(c.toString());
			if (valid)
				str.append(String.format("%s: %s%n", c, c.name()));			
		}
		return str.toString();		
	}
	
	/**
	 * It checks whether a String matches an strategy
	 * @param strat String to validate in UpperCase
	 * @return true if strat matches an Strategy and false otherwise.
	 */
	protected boolean validStrategy(String strat) {
		for (Strategy s : Strategy.strategies) {
			if(s.toString().equals(strat)) {
				return true;
			}
		}
		return false;
	}

}
