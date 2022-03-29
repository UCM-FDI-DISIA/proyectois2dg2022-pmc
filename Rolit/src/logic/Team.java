package logic;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class Team implements Reportable {
	private Color color;
	private String name;
	private int score;
	private List<Player> players;
	
	public Team(String name, Color color, List<Player> list_players) {
		this.color = color;
		this.name = name;
		this.score = 0;
		this.players = new ArrayList<>();
	}
	
	public void update() {
		this.score = 0;
		for (Player p : players)
			this.score += p.getScore();
	}

	@Override
	public JSONObject report() {
		JSONObject teamJSONObject = new JSONObject();
		teamJSONObject.put("name", this.name);
		teamJSONObject.put("color", this.color.toString());
		JSONArray playersJSONObject = new JSONArray();
		for (Player p : this.players)
			playersJSONObject.put(p.report());
		teamJSONObject.put("players", playersJSONObject);
		return teamJSONObject;
	}

}
