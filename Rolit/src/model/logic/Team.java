package model.logic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * This class represents a team form GameTeams mode
 * @author PMC
 *
 */
public class Team implements Reportable, Rival {
	private static Map<String, Team> relations = new HashMap<>();
	private String name;
	private int score;
	private List<Player> players;
	public static final String TYPE = "Team";
	
	/**
	 * Constructor
	 * @param name Team Name
	 * @param list_players Players in the team
	 */
	public Team(String name, List<Player> list_players) {
		this.name = name;
		this.score = 0;
		this.players = list_players;
		for (Player p : this.players)
			relations.put(p.getName(), this);
	}
	
	/**
	 * Constructor with initial score
	 * @param name Team Name
	 * @param list_players Players in the team
	 * @param score Initial score
	 */
	public Team(String name, List<Player> list_players, int score) {
		this(name, list_players);
		this.score = score;
	}
	
	/**
	 * This method updates the scores of the team
	 */
	public void update() {
		this.score = 0;
		for (Player p : players)
			this.score += p.getScore();
	}
	
	/**
	 * It returns the team of a given player p
	 * @param p Player to search
	 * @return The team of the player p
	 */
	public static Team getTeam(Player p) {
		return relations.get(p.getName());
	}
	
	public String getName() {
		return this.name; 
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


}
