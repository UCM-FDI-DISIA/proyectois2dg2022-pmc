package model.builders;

import org.json.JSONObject;

import model.logic.Game;

/**
 * Clase encargada de crear el juego
 * @author PMC
 *
 */
public abstract class GameBuilder {
	private static GameBuilder[] builders = {
		new GameClassicBuilder(),
		new GameTeamsBuilder()
	};
	

	/**
	 * Genera una cadena de caracteres por medio de un stringBuilder con todos los tipos de juegos, en este caso GameClassic y GameTeams
	 * @return devuelve dicha cadena de caracteres 
	 */
	public static String availableModes() {
		StringBuilder str = new StringBuilder();
		for (GameBuilder g : builders) {
			str.append(" " + g.getName());
		}
		str.append(": ");
		return str.toString();
	}
	
	/**
	 * Comprueba que el tipo de juego que se le pasa por parámetro coincide con uno de los disponibles, por medio del uso del método parse
	 * @param type es una cadena de caracteres que indican un tipo de juego
	 * @return devuelve true si type es uno de los tipos de juego disponibles y false en caso contrario
	 */
	public static boolean isAvailableMode(String type) {
		return GameBuilder.parse(type) != null;
	}
	
	/**
	 * Comprueba que el tipo de juego que se le pasa por parámetro coincide con uno de los disponibles
	 * @param type es una cadena de caracteres que indican un tipo de juego
	 * @return devuelve el correspondiente builder de dicho tipo de juego si type es uno de los tipos de juego disponibles y null en caso contrario
	 */
	private static GameBuilder parse(String type) {
		for (GameBuilder g : builders) {
			if (g.match(type))
				return g;
		}
		return null;
	}

	/**
	 * Por medio de un JSONObject se crea un nuevo game, en caso de que el tipo de juego que contiene este JSONObject
	 * no sea uno de los disponibles (nos lo dirá el método parse) se lanza una excepción, en otro caso, generaremos el juego.
	 * @param o JSONObject que contiene la información del juego que se quiere crear
	 * @return devuelve el juego ya generado
	 */
	public static Game createGame(JSONObject o) {
		String type = o.getString("type");
		GameBuilder gameGen = GameBuilder.parse(type);
		// FIXME esto para la aplicaci�n
		if (gameGen == null)
			throw new IllegalArgumentException("The game mode mustnt be null");
		else
			return gameGen.GenerateGame(o);		
	}
	
	protected abstract boolean match(String type);
	protected abstract Game GenerateGame(JSONObject o);
	protected abstract String getName();
	
}
