package replay;



import org.json.JSONArray;
import org.json.JSONObject;

import logic.Replayable;
import logic.Reportable;
import utils.StringUtils;

public class State implements Reportable {
	String command;
	Replayable game;
	
	public State(String commandName, Replayable game){
		this.command = commandName;
		this.game = game;
	}
	
	public State(Replayable game) {
		this.command = "";
		this.game = game;
	}
	
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append("Command > ").append(command).append(StringUtils.LINE_SEPARATOR);
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
	
	public JSONArray getCubes() {
		return game.report().getJSONObject("board").getJSONArray("cubes");
	}
	
	public char getTurnColorShorcut() {
		return game.report().getString("turn").charAt(0);
	}
	
	private int findTurnPlayer() {
		String shortcut = String.valueOf(getTurnColorShorcut());
		JSONArray players = game.report().getJSONArray("players");
		int i = 0;
		boolean found = false;;
		while(i < players.length() && !found) {
			if(shortcut.equals(players.getJSONObject(i).getString("color"))) {
				found = true;
			}
			else
				i++;
		}
		return i % players.length();
	}
	
	
	public String getShape(){
		return game.report().getJSONObject("board").getString("shape");
	}
	
	public JSONArray getRivals() {
		JSONObject gameReport = game.report();
		if(gameReport.has("teams")) 
			return gameReport.getJSONArray("teams");
		else
			return gameReport.getJSONArray("players");
	}
	
	public String getType() {
		return game.report().getString("type");
	}
	
	public String getCommand() {
		return this.command;
	}
	
	public String getFirstPlayerName() {
		JSONArray players = game.report().getJSONArray("players");
		return players.getJSONObject(0).getString("name");
	}
	
	public char getFirstPlayerColorShortcut() {
		JSONArray players = game.report().getJSONArray("players");
		return players.getJSONObject(0).getString("color").charAt(0);
	}

	public String getTurnName() {
		JSONArray players = game.report().getJSONArray("players");
		return players.getJSONObject(findTurnPlayer()).getString("name");
	}
}
