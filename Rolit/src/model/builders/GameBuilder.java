package model.builders;

import org.json.JSONObject;

import model.logic.Game;

/**
 * This class is in charge of creating the game
 * @author PMC
 *
 */
public abstract class GameBuilder {
	/**
	 * Constructor
	 */
	private static GameBuilder[] builders = {
		new GameClassicBuilder(),
		new GameTeamsBuilder()
	};
	

	/** 
	 * Generates a string by means of a stringBuilder with all the game types, in this case GameClassic y GameTeams 
	 * @return Returns that string
	 */
	public static String availableModes() {
		StringBuilder str = new StringBuilder();
		for (GameBuilder g : builders) {
			str.append(g.getName() + ", ");
		}
		return str.toString().substring(0, str.length() - 2);
	}
	
	/**
	 * It checks whether the game as a parameter is equal to any of the available modes
	 * @param type Type of the game 
	 * @return True if the parameter is equal to any of the available modes or false otherwise
	 */
	public static boolean isAvailableMode(String type) {
		return GameBuilder.parse(type) != null;
	}
	
	/**
	 * It checks whether the game as a parameter is equal to any of the available modes
	 * @param type Type of the game
	 * @return The appropriate GameBuilder if the parameter is equal to any of the available modes or null otherwise
	 */
	private static GameBuilder parse(String type) {
		for (GameBuilder g : builders) {
			if (g.match(type))
				return g;
		}
		return null;
	}

	/**
	 * It creates the appropriate new Game
	 * @param o JSONObject that contains the information of the game that we are trying to create
	 * @return An already created game
	 */
	public static Game createGame(JSONObject o) {
		String type = o.getString("type");
		GameBuilder gameGen = GameBuilder.parse(type);
		if (gameGen == null)
			throw new IllegalArgumentException("The game mode mustnt be null");
		else
			return gameGen.GenerateGame(o);		
	}
	
	/**
	 * It checks whether the game as a parameter is equal to any of the available modes
	 * @param type Type of the game
	 * @return true If the parameter is equal to any of the available modes or false otherwise
	 */
	protected abstract boolean match(String type);
	
	/**
	 * It Is in charge of creating the game
	 * @param o JSONObject that contains the information of the game that we are trying to create
	 * @return An already created game
	 */
	protected abstract Game GenerateGame(JSONObject o);
	
	/**
	 * It returns the type of the class that we are trying to create
	 * @return An string with the type type of the class that we are trying to create
	 */
	protected abstract String getName();
	
}
