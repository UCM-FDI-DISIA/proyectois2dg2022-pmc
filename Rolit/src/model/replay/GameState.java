package model.replay;

import org.json.JSONArray;
import org.json.JSONObject;
import model.logic.Replayable;
import model.logic.Reportable;

/**
 * This class represent the game in an instance of time.
 * @author PMC
 *
 */
public class GameState implements Reportable {
	
	private String command;
	private Replayable game;
	
	/**
	 * Constructor
	 * @param commandName Command that generated this state
	 * @param game Replayable copy of the current game
	 */
	public GameState(String commandName, Replayable game){
		this.command = commandName;
		this.game = game;
	}
	
	/**
	 * Constructor
	 * @param game Replayable copy of the current game
	 */
	public GameState(Replayable game) {
		this(null, game);
	}
	
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append(game.toString());
		return str.toString();
	}
	
	@Override
	public JSONObject report() {
		JSONObject jo = new JSONObject();
	
		jo.put("command", command);
		jo.put("game", game.report());
		
		return jo;
		
	}
	
	/**
	 * It returns a JSONArray containing the cubes in the board
	 * @return JSONArray containing the cubes in the board
	 */
	public JSONArray getCubes() {
		return game.report().getJSONObject("board").getJSONArray("cubes");
	}
	
	/**
	 * It returns the color shortcut of the current player in an state
	 * @return The color shortcut of the current player in an state
	 */
	public char getTurnColorShortcut() {
		return game.report().getString("turn").charAt(0);
	}
	
	/**
	 * It returns the index of the current player in the JSONArray of players of the game.report()
	 * @return The index of the current player in the JSONArray of players of the game.report()
	 */
	private int findTurnPlayer() {
		String shortcut = String.valueOf(getTurnColorShortcut());
		JSONArray players = game.report().getJSONArray("players");
		int i = 0;
		boolean found = false;
		while(i < players.length() && !found) {
			if(shortcut.equals(players.getJSONObject(i).getString("color"))) {
				found = true;
			}
			else
				i++;
		}
		return i % players.length();
	}
	
	/**
	 * It returns the name of board shape
	 * @return The name of board shape
	 */
	public String getShape(){
		return game.report().getJSONObject("board").getString("shape");
	}
	
	/**
	 * It returns a JSONArray containing the rivals
	 * @return JSONArray containing the rivals
	 */
	public JSONArray getRivals() {
		JSONObject gameReport = game.report();
		if(gameReport.has("teams")) 
			return gameReport.getJSONArray("teams");
		else
			return gameReport.getJSONArray("players");
	}
	
	/**
	 * It returns the name of the game mode
	 * @return The name of the game mode
	 */
	public String getType() {
		return game.report().getString("type");
	}
	
	/**
	 * It returns the command that generated the state
	 * @return The command that generated the state (it might be null)
	 */
	public String getCommand() {
		return this.command;
	}

	/**
	 * It returns the name of the current player
	 * @return The name of the current player
	 */
	public String getTurnName() {
		JSONArray players = game.report().getJSONArray("players");
		return players.getJSONObject(findTurnPlayer()).getString("name");
	}
}
