package logic;

import org.json.JSONObject;

public class Player implements Comparable<Player>, Reportable {
	private static Player[] playerList = new Player[Color.size()];
	private Color color;
	private int score;
	private String name;

	// constructor habitual de un player por defecto
	public Player(Color c, String name) {
		this.color = c;
		this.score = 0;
		this.name = name;
		Player.playerList[color.ordinal()] = this;
	}	
	
	public Color getColor() {
		return this.color;
	}
	
	public int getScore() {
		return this.score;
	}

	public void addScore(int score) {
		this.score += score;
	}

	@Override
	public int compareTo(Player p) {
		return -(this.score - p.score);	//Lleva un - delante para que no sea orden natural, sino mayor a menor
	}
	
	public String getName() {
		return this.name;
	}
	
	public static Player getPlayer(Color c) {
		return Player.playerList[c.ordinal()];
	}

	@Override
	public JSONObject report() {
		JSONObject jo1 = new JSONObject();
		jo1.put("name", name);
		jo1.put("color", color.toString());
		
		return jo1;
	}

	@Override
	public String toString() {
		return name + " (" + color.toString() + ")";
	}

}
