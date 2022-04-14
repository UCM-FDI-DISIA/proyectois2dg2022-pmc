package Builders;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

import logic.Board;
import logic.Color;
import logic.Cube;
import logic.Game;
import logic.GameClassic;
import logic.Player;
import logic.Shape;

public class GameClassicBuilder extends GameBuilder {
	public static final String TYPE = "GameClassic";
	private static final String NAME_PLAYERS = "Name the players: ";
	private static final String CHOOSE_COLOR = "Choose a color shortcut: ";

	GameClassicBuilder(){
		
	};
	
	@Override
	protected Game GenerateGame(JSONObject o) {
		Color turn = Color.valueOfIgnoreCase(o.getString("turn").charAt(0));
		
		List<Player> list_players = new ArrayList<Player>();
		JSONArray playersJSONArray = o.getJSONArray("players");
		for (int i = 0; i < playersJSONArray.length(); ++i)
			list_players.add(new Player(Color.valueOfIgnoreCase(playersJSONArray.getJSONObject(i).getString("color").charAt(0)),
					playersJSONArray.getJSONObject(i).getString("name")));

		JSONObject boardJSONObject = o.getJSONObject("board");
		Board board = new Board(Shape.valueOfIgnoreCase(boardJSONObject.getString("shape"))); // FIXME asumo que el constructor de Board se ve
		
		List<Cube> list_cubes = new ArrayList<Cube>();
		JSONArray cubesJSONArray = boardJSONObject.getJSONArray("cubes");
		for (int i = 0; i < cubesJSONArray.length(); ++i)
			list_cubes.add(new Cube(cubesJSONArray.getJSONObject(i).getJSONArray("pos").getInt(0), cubesJSONArray.getJSONObject(i).getJSONArray("pos").getInt(1),
					Player.getPlayer(Color.valueOfIgnoreCase(cubesJSONArray.getJSONObject(i).getString("color").charAt(0)))));
		return new GameClassic(board, list_cubes, list_players, turn);
	}

	@Override
	protected boolean match(String type) {
		return TYPE.equals(type);
	}

	@Override
	protected String getName() {
		return TYPE;
	}	
}
