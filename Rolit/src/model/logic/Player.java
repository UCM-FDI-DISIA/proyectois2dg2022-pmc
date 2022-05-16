package model.logic;

import org.json.JSONObject;
import model.replay.GameState;
import model.strategy.Strategy;
import utils.Pair;

/**
 * This class represents a Rolit player
 * @author PMC
 *
 */
public class Player implements Reportable, Rival {
	/**
	 * List of players
	 */
	private static Player[] playerList = new Player[Color.size()];
	
	/**
	 * Player color
	 */
	private Color color;
	
	/**
	 * Player score
	 */
	private int score;
	
	/**
	 * Player name
	 */
	private String name;
	
	/**
	 * Player strategy (null for real players)
	 */
	private Strategy strategy;
	

	/**
	 * Constructor for player with no strategy
	 * @param c Player color
	 * @param name Player name
	 */
	public Player(Color c, String name) {
		this(c, name, null);
	}	
	
	/**
	 * Constructor for player with strategy
	 * @param c Player color
	 * @param name Player name
	 * @param strat Player strategy
	 */
	public Player(Color c, String name, Strategy strat) {
		this.color = c;
		this.score = 0;
		this.name = name;
		Player.playerList[color.ordinal()] = this;
		this.strategy = strat;
	}
	
	/**
	 * Constructor: build player from JSONObject
	 * @param json Player report
	 */
	public Player(JSONObject json) {
		this(Color.valueOfIgnoreCase(json.getString("color").charAt(0)), json.getString("name"));
	}
	
	/**
	 * Contructor: build player from JSONObject with an strategy
	 * @param json Player report
	 * @param s Player strategy
	 */
	public Player(JSONObject json, Strategy s) {
		this(json);
		this.strategy = s;
	}
	
	/**
	 * If the player has a strategy, it is executed
	 * @param state Current GameState
	 * @return Coordinates to place cube, if the player has no strategy it returns null
	 */
	public Pair<Integer, Integer> play(GameState state) {
		if(this.strategy != null) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Pair<Integer, Integer> coor = this.strategy.calculateNextMove(color, state);
			try {
				if(coor != null)
					return coor;
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		return null;
	}
	
	/**
	 * It returns the player color
	 * @return Player color
	 */
	public Color getColor() {
		return this.color;
	}
	


	/**
	 * It adds an score to a rival
	 * @param score Score to add (it can be negative)
	 */
	public void addScore(int score) {
		this.score += score;
	}
	
	public String getName() {
		return this.name;
	}
	

	public int getScore() {
		return this.score;
	}
	
	/**
	 * This method returns the player with color c
	 * @param c Player color
	 * @return Player with color c
	 */
	public static Player getPlayer(Color c) {
		return Player.playerList[c.ordinal()];
	}

	@Override
	public JSONObject report() {
		JSONObject jo1 = new JSONObject();
		jo1.put("name", name);
		jo1.put("score", score);
		jo1.put("color", color.toString());
		if(strategy != null)
			jo1.put("strategy", strategy.toString());
		return jo1;
	}

	@Override
	public String toString() {
		return name + " (" + color.toString() + ")";
	}

}
