package Builders;

import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

import logic.Board;
import logic.Color;
import logic.Game;
import logic.GameClassic;
import logic.Shape;

public abstract class GameBuilder {	
	private static final String PROMPT = "> ";
	private static final String ERROR_MODES_MSG = "Game mode must be on of these:";
	private static final String NUMBER_PLAYERS_MSG = "Choose the number of players [2 - " + Color.size() +"]";
	private static final String ERROR_PLAYERS_MSG = "Number of players must be a number between 2 and " + Color.size() + " (inclusive)";
	private static final String BOARD_MSG = "Choose a board shape. ";
	private static final String BOARD_ERROR = "ERROR: Not found such shape. ";
	protected static final String NAME_PLAYERS = "Name the players: ";
	protected static final String CHOOSE_COLOR = "Choose a color shortcut: ";
	protected final static Scanner input = new Scanner(System.in);	
	private static GameBuilder[] builders = {
		new GameClassicBuilder(),
		new GameTeamsBuilder()
	};
	private static String AVAILABLE_MODES_MSG = "Choose game mode between" + GameBuilder.availableModes();
	
	private static String availableModes() {
		StringBuilder str = new StringBuilder();
		for (GameBuilder g : builders) {
			str.append(" " + g.getType());
		}
		str.append(": ");
		return str.toString();
	}
	
	private static JSONObject ask() {
		JSONObject o = new JSONObject();
		// elegimos el modo de juego concreto
		System.out.println(AVAILABLE_MODES_MSG);
		String type = input.nextLine();
		while(GameBuilder.parse(type) == null) {
			System.out.println(GameBuilder.ERROR_MODES_MSG);
			type = input.nextLine();
		}
		o.put("type", type);
		// Elegimos el numero de jugadores
		System.out.println(NUMBER_PLAYERS_MSG);
		int nPlayers = input.nextInt();
		while (nPlayers < 2 || nPlayers > Color.size()) {
			System.out.println(ERROR_PLAYERS_MSG);
			nPlayers = input.nextInt();
		}
		// Elegimos el tablero concreto
		System.out.print(BOARD_MSG);
		String board_shape;
		do {
			System.out.print(Shape.availableShapes());
			System.out.print(PROMPT);
			board_shape = input.nextLine();
			if (Shape.valueOfIgnoreCase(board_shape) == null) {
				System.out.print(BOARD_ERROR);
			}
		} while (Shape.valueOfIgnoreCase(board_shape) == null);		
		o.put("Board", new Board(Shape.valueOfIgnoreCase(board_shape)).report());
		// TODO faltan preguntar a los método específicos de cada factoría
		parse(type).whatINeed(nPlayers, o);
		
		return o;
	}
	
	protected final JSONObject validatePlayer(JSONArray players, String name, Color color) {
		JSONObject jPlayer = new JSONObject();
		if (color == null)
			 throw new IllegalArgumentException("The shortcut does not match any color");
		for (int j = 0; j < players.length(); j++) {
			if (players.getJSONObject(j).get("color").equals(color.toString()))
			 	 throw new IllegalArgumentException("The selected color is not available");
		}
		jPlayer.put("name", name);
		jPlayer.put("color", color.toString());
		return jPlayer;
	}
	
	protected final String availableColors(JSONArray players) {
		StringBuilder str = new StringBuilder();
		for (Color c : Color.values()) {
			for (int j = 0; j < players.length(); j++) {
				 if (!players.getJSONObject(j).get("color").equals(c.toString()))
					 str.append(String.format("%s: %s%n", c, c.name()));
			 }
		}
		return str.toString();		
	}
	
	private static GameBuilder parse(String type) {
		for (GameBuilder g : builders) {
			if (g.match(type))
				return g;
		}
		return null;
	}

	public static Game createGame(JSONObject o) {
		String type = o.getString("type");
		GameBuilder gameGen = GameBuilder.parse(type);
		// FIXME esto para la aplicación
		if (gameGen == null)
			throw new IllegalArgumentException("The game mode mustnt be null");
		else
			return gameGen.GenerateGame(o);		
	}
	
	public static Game createGame() {
		JSONObject o = GameBuilder.ask();
		return createGame(o); 
	}
	
	protected abstract void whatINeed(int nPlayers, JSONObject o);
	protected abstract boolean match(String type);
	protected abstract Game GenerateGame(JSONObject o);
	// FIXME no me convence demasiado
	abstract String getType();
	
}
