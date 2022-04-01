package logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

public class Team implements Reportable, Comparable<Team> {
	private static Map<Player, Team> relations = new HashMap<>();
	private String name;
	private int score;
	private List<Player> players;
	
	public Team(String name, List<Player> list_players) {
		this.name = name;
		this.score = 0;
		this.players = list_players;
		for (Player p : this.players)
			relations.put(p, this);
	}
	
	@Override
	public int compareTo(Team o) {
		return -(this.score - o.score);		// lleva el menos delante para denotar que es el >
	}
	
	public void update() {
		this.score = 0;
		for (Player p : players)
			this.score += p.getScore();
	}
	
	public static Team getTeam(Player p) {
		return relations.get(p);
	}
	
	public int getScore() {
		return this.score;
	}
	
	@Override
	public String toString() {
		return this.name;
	}

	@Override
	public JSONObject report() {
		JSONObject teamJSONObject = new JSONObject();
		teamJSONObject.put("name", this.name);
		JSONArray playersJSONObject = new JSONArray();
		for (Player p : this.players)
			playersJSONObject.put(p.report());
		teamJSONObject.put("players", playersJSONObject);
		return teamJSONObject;
	}

}
