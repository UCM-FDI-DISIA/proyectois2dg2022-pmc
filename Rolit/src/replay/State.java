package replay;


import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import logic.Board;
import logic.Color;
import logic.Cube;
import logic.Game;
import logic.GameClassic;
import logic.GameTeams;
import logic.Player;
import logic.Replayable;
import logic.Reportable;
import logic.Shape;
import logic.Team;
import utils.StringUtils;

public class State implements Reportable {
	String command;
	Replayable game;
	
	public State(String commandName, Replayable game){
		this.command = commandName;
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
	
	public String getShape(){
		return game.report().getJSONObject("board").getString("shape");
	}
}
