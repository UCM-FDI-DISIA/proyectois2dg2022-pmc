package Builders;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import logic.Board;
import logic.Color;
import logic.Cube;
import logic.Game;
import logic.GameTeams;
import logic.Player;
import logic.Shape;
import logic.Team;

public class GameTeamsBuilder extends GameBuilder {
	public static final String TYPE = "GameTeams";

	GameTeamsBuilder() {
				
	}
	
	@Override
	protected Game GenerateGame(JSONObject o) {		
		Color turn = Color.valueOfIgnoreCase(o.getString("turn").charAt(0));
		
		List<Player> list_players = new ArrayList<Player>();
		JSONArray playersJSONArray = o.getJSONArray("players");
		for (int i = 0; i < playersJSONArray.length(); ++i) {
			JSONObject player = playersJSONArray.getJSONObject(i);
			list_players.add(new Player(Color.valueOfIgnoreCase(player.getString("color").charAt(0)), player.getString("name")));
		}			

		JSONObject boardJSONObject = o.getJSONObject("board");
		Board board = new Board(Shape.valueOfIgnoreCase(boardJSONObject.getString("shape"))); // FIXME asumo que el constructor de Board se ve	
		
		JSONArray teamsJSONArray = o.getJSONArray("teams");
		List<Team> list_teams = new ArrayList<Team>();
		for (int i = 0; i < teamsJSONArray.length(); i++) {
			JSONObject team = teamsJSONArray.getJSONObject(i);
			List<Player> list_playersTeam = new ArrayList<Player>();
			JSONArray playersTeamJSONArray = team.getJSONArray("players");
			for (int j = 0; j < playersTeamJSONArray.length(); ++j) {
				JSONObject playerTeam = playersTeamJSONArray.getJSONObject(j);
				list_playersTeam.add(new Player(Color.valueOfIgnoreCase(playerTeam.getString("color").charAt(0)), playerTeam.getString("name")));
			}
			list_teams.add(new Team(team.getString("name"), list_playersTeam));			
		}
		
		List<Cube> list_cubes = new ArrayList<Cube>();
		JSONArray cubesJSONArray = boardJSONObject.getJSONArray("cubes");
		for (int i = 0; i < cubesJSONArray.length(); ++i) {
			JSONObject cube = cubesJSONArray.getJSONObject(i);
			list_cubes.add(new Cube(cube.getJSONArray("pos").getInt(0), cube.getJSONArray("pos").getInt(1),
					Player.getPlayer(Color.valueOfIgnoreCase(cube.getString("color").charAt(0)))));
		}		
		
		return new GameTeams(board, list_cubes, list_players, turn, list_teams);
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
