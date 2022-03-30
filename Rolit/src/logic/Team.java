package logic;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import control.Controller;

public class Team implements Reportable {
	private Color color;
	private String name;
	private int score;
	private List<Player> players;
	
	public Team(String name, Color color, List<Player> list_players) {
		this.color = color;
		this.name = name;
		this.score = 0;
		this.players = list_players;
	}
	
	public void update() {
		this.score = 0;
		for (Player p : players)
			this.score += p.getScore();
	}

	
	//lo cree para los test, si no lo necesitamos lo borramos y ya
	public int getScore() {
		return score;
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
	
	public static void main(String[] args) {
		Player p1 = new Player(Color.YELLOW, "Chacon Chacon");
		Player p2 = new Player(Color.YELLOW, "Leonardo Macias Pasteles");
		List<Player> l = new ArrayList<Player>();
		l.add(p1); l.add(p2);
		Team equipo1 = new Team("Basados", Color.YELLOW, l);
		
		System.out.print(equipo1.report());
	}

}
