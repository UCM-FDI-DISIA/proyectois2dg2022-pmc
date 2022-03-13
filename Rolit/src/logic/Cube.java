package logic;

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

	public String serialize() {
		return (this.toString() + " " + x + " " + y);
	}

	@Override
	public JSONObject report() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
