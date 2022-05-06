package model.logic;

import org.json.JSONArray;
import org.json.JSONObject;

public class Cube implements Reportable {
	private int x;
	private int y;
	private Player player;
	private int value = 1;
	
	public Cube(int x, int y, Player player) {
		this.x = x;
		this.y = y;
		this.player = player;
	}

	public Cube(Cube cube) {
		this.x = cube.x;
		this.y = cube.y;
		this.player = cube.player;
	}

	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}

	public Color getColor() {
		return player.getColor();
	}
	
	public void changeOwner(Color color) {
		player.addScore(-value);
		this.player = Player.getPlayer(color);
		player.addScore(value);
	}
	
	public void addPlayerScore() {
		player.addScore(value);
	}
	
	@Override
	public String toString() {
		return this.getColor().toString();
	}
	
	@Override
	public JSONObject report() {
		
		JSONObject jo = new JSONObject();
		
		jo.put("color", player.getColor().toString());
		
		JSONArray jo1 = new JSONArray();
		jo1.put(x);
		jo1.put(y);
		
		jo.put("pos", jo1);
		
		return jo;
	}
	
}
