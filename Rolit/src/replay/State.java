package replay;


import org.json.JSONObject;
import logic.Replayable;
import logic.Reportable;
import utils.StringUtils;

public class State implements Reportable {
	String command;
	Replayable game;
	
	State(String commandName, Replayable game){
		this.command = commandName;
		this.game = game;
	}
	
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append("Command > ").append(command).append(StringUtils.LINE_SEPARATOR);
		str.append(game);
		return str.toString();
	}
	
	@Override
	public JSONObject report() {
		JSONObject jo = new JSONObject();
	
		jo.put("command", command);
		jo.put("game", game.report());
		
		return jo;
		
	}
	
	public String getShape(){
		return game.report().getJSONObject("board").getString("shape");
	}
}
