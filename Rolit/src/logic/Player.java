package logic;

import org.json.JSONObject;

public class Player implements Comparable<Player>, Reportable, Rival {
	protected static Player[] playerList = new Player[Color.size()];
	protected Color color;
	protected int score;
	protected String name;
	public static final String TYPE = "Player";

	// constructor habitual de un player por defecto
	public Player(Color c, String name) {
		this.color = c;
		this.score = 0;
		this.name = name;
		Player.playerList[color.ordinal()] = this;
	}	

	public Player(Player player) {
		this.color = player.color;
		this.score = player.score;
		this.name = player.name;
	}

	public Player(JSONObject json) {
		this(Color.valueOfIgnoreCase(json.getString("color").charAt(0)), json.getString("name"));
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
		jo1.put("score", score);
		jo1.put("color", color.toString());
		
		return jo1;
	}

	@Override
	public String toString() {
		return name + " (" + color.toString() + ")";
	}

	@Override
	public String getType() {
		return TYPE;
	}

}
