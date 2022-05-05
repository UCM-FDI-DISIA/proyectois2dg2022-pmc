package logic;

import org.json.JSONObject;

import Strategy.Strategy;
import commands.PlaceCubeCommand;
import replay.GameState;
import utils.Pair;

public class Player implements Reportable, Rival {
	private static Player[] playerList = new Player[Color.size()];
	private Color color;
	private int score;
	private String name;
	private Strategy strategy;
	public static final String TYPE = "Player";
	
	// constructor habitual de un player por defecto
	public Player(Color c, String name) {
		this(c, name, null);
	}	

	public Player(Color c, String name, Strategy strat) {
		this.color = c;
		this.score = 0;
		this.name = name;
		Player.playerList[color.ordinal()] = this;
		this.strategy = strat;
	}
	
	public Player(Player player) {
		this.color = player.color;
		this.score = player.score;
		this.name = player.name;
		this.strategy = player.strategy;	//FIXME Esto puede dar algun problema, no es seguro
	}
	
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
		if(strategy != null)
			jo1.put("strategy", strategy.toString());
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
