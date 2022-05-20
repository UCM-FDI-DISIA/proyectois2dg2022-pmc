package model.builders;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import model.logic.Board;
import model.logic.Color;
import model.logic.Cube;
import model.logic.Game;
import model.logic.GameClassic;
import model.logic.Player;
import model.logic.Shape;
import model.strategy.Strategy;

/**
 * This class extends GameBuilder and is in charge of creating the classic game
 * @author PMC
 *
 */
public class GameClassicBuilder extends GameBuilder {
	public static final String TYPE = "GameClassic";
	
	/**
	 * Constructor
	 */
	GameClassicBuilder(){
		
	}
	
	@Override
	protected Game GenerateGame(JSONObject o) {
		Color turn = Color.valueOfIgnoreCase(o.getString("turn").charAt(0));
		
		List<Player> list_players = new ArrayList<Player>();
		JSONArray playersJSONArray = o.getJSONArray("players");
		for (int i = 0; i < playersJSONArray.length(); ++i) {
			Color c = Color.valueOfIgnoreCase(playersJSONArray.getJSONObject(i).getString("color").charAt(0));
			Strategy s = null;
			if(playersJSONArray.getJSONObject(i).has("strategy")) {
				s = Strategy.parse(c, playersJSONArray.getJSONObject(i).getString("strategy"));
			}
			list_players.add(new Player(playersJSONArray.getJSONObject(i), s));
		}
		
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
