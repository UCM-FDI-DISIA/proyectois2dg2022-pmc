package view.consoleView;

import org.json.JSONArray;
import org.json.JSONObject;

import Builders.GameBuilder;
import Strategy.Strategy;
import logic.Board;
import logic.Color;
import logic.Shape;

public class NewGameWindow implements ConsoleWindow {
	private static final String NUMBER_PLAYERS_MSG = "Choose the number of players [2 - " + Color.size() +"]";
	private static final String ERROR_MODES_MSG = "Game mode must be on of these:";
	private static final String ERROR_PLAYERS_MSG = "Number of players must be a number between 2 and " + Color.size() + " (inclusive)";
	private static String AVAILABLE_MODES_MSG = "Choose game mode between" + GameBuilder.availableModes();
	private static final String BOARD_MSG = "Choose a board shape. ";
	private static final String BOARD_ERROR = "ERROR: Not found such shape. ";
	protected static final String NAME_PLAYERS = "Name the players (if you want a player to be an AI, end the name with AI (i.e. Mark AI): ";
	protected static final String CHOOSE_COLOR = "Choose a color shortcut: ";
	protected static final String CHOOSE_AI_DIFFICULTY_MSG = "Choose the AI difficulty: ";
	
	private static final String TYPE = "Game";
	private	static NewGameWindow[] everyGameWindows = { new NewGameClassicWindow(), new NewGameTeamsWindow() };
	protected static int nPlayers;	
	protected static JSONObject json;
	
	@Override
	public boolean open() {
		// FIXME todas las excepciones de meter cosas mal deberían ser excepciones
		json = new JSONObject();
		// elegimos el modo de juego concreto
		System.out.println(AVAILABLE_MODES_MSG);
		String type = input.nextLine();
		while(!GameBuilder.isAvailableMode(type)) {
			System.out.println(ERROR_MODES_MSG);
			type = input.nextLine();
		}
		json.put("type", type);
		// Elegimos el numero de jugadores
		System.out.println(NUMBER_PLAYERS_MSG);
		nPlayers = input.nextInt();
		input.nextLine();
		while (nPlayers < 2 || nPlayers > Color.size()) {
			System.out.println(ERROR_PLAYERS_MSG);
			nPlayers = input.nextInt();
			input.nextLine();
		}
		// Elegimos el tablero concreto
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
	
	private NewGameWindow parse(String type) {
		for (NewGameWindow g : everyGameWindows) {
			if (g.match(type))
				return g;
		}
		return null;
	}
	
	protected boolean match(String type) {
		return NewGameWindow.TYPE.equals(type);
	}

	@Override
	public Object get() {
		return NewGameWindow.json;
	}
	
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

}
