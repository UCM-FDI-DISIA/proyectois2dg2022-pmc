package logic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

public class Team implements Reportable, Rival {
	private static Map<String, Team> relations = new HashMap<>();
	private String name;
	private int score;
	private List<Player> players;
	public static final String TYPE = "Team";
	
	public Team(String name, List<Player> list_players) {
		this.name = name;
		this.score = 0;
		this.players = list_players;
		for (Player p : this.players)
			relations.put(p.getName(), this);
	}
	
	public Team(String name, List<Player> list_players, int score) {
		this(name, list_players);
		this.score = score;
	}
	
	public void update() {
		this.score = 0;
		for (Player p : players)
			this.score += p.getScore();
	}
	
	public static Team getTeam(Player p) {
		return relations.get(p.getName());
	}
	
	public int getScore() {
		return this.score;
	}
	
	@Override
	public String toString() {
		return this.getName();
	}

	@Override
	public JSONObject report() {
		JSONObject teamJSONObject = new JSONObject();
		teamJSONObject.put("name", this.name);
		teamJSONObject.put("score", this.score);
		JSONArray playersJSONObject = new JSONArray();
		for (Player p : this.players)
			playersJSONObject.put(p.report());
		teamJSONObject.put("players", playersJSONObject);
		return teamJSONObject;
	}
	
	public String getName() {
		return this.name; 
	}

}
