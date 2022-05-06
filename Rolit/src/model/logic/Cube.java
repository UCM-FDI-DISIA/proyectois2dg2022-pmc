package model.logic;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * This class represents a cube tile from the Rolit game
 * @author PMC
 *
 */
public class Cube implements Reportable {
	private int x;
	private int y;
	private Player player;
	private int value = 1;
	
	/**
	 * Constructor
	 * @param x x coordinate of the cube
	 * @param y y coordinate of the cube
	 * @param player Cube owner
	 */
	public Cube(int x, int y, Player player) {
		this.x = x;
		this.y = y;
		this.player = player;
	}
	
	/**
	 * Copy constructor (deep copy)
	 * @param cube Cube to copy
	 */
	public Cube(Cube cube) {
		this.x = cube.x;
		this.y = cube.y;
		this.player = cube.player;
	}

	/**
	 * Returns the x coordinate of the cube
	 * @return x coordinate
	 */
	public int getX() {
		return x;
	}
	
	/**
	 * Returns the y coordinate of the cube
	 * @return y coordinate
	 */
	public int getY() {
		return y;
	}

	/**
	 * Returns the color of the cube
	 * @return Color of the cube
	 */
	public Color getColor() {
		return player.getColor();
	}
	
	/**
	 * This method changes the player owner of the cube to the one associated to the parameter color
	 * @param color Color of the new owner
	 */
	public void changeOwner(Color color) {
		player.addScore(-value);
		this.player = Player.getPlayer(color);
		player.addScore(value);
	}
	
	/**
	 * This method adds to the cube owner its score value
	 */
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
