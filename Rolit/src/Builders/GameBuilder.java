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
	private static GameBuilder[] builders = {
		new GameClassicBuilder(),
		new GameTeamsBuilder()
	};
	
	public static String availableModes() {
		StringBuilder str = new StringBuilder();
		for (GameBuilder g : builders) {
			str.append(" " + g.getName());
		}
		str.append(": ");
		return str.toString();
	}
	
	public static boolean isAvailableMode(String type) {
		return GameBuilder.parse(type) != null;
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
