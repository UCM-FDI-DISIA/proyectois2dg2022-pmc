package replay;

import java.util.List;

import org.json.JSONObject;

import commands.Command;
import logic.Board;
import logic.Cube;
import logic.Player;
import logic.Replayable;
import utils.StringUtils;

public class State {
	String playerName;
	String colorShortcut;
	String command;
	Replayable board;
	
	State(String playerName, String colorShortcut, String commandName, Replayable board){
		this.playerName = playerName;
		this.colorShortcut = colorShortcut;
		this.command = commandName;
		this.board = board;
	}
	
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append("Turn: ").append(playerName + " (" + colorShortcut + ")").append(StringUtils.LINE_SEPARATOR);
		str.append("Command > ").append(command).append(StringUtils.LINE_SEPARATOR);
		str.append(board);
		return str.toString();
	}
	
	public JSONObject report() {
		JSONObject jo = new JSONObject();
		JSONObject turn = new JSONObject();
		turn.put("name", playerName);
		turn.put("color", colorShortcut);
		
		jo.put("turn", turn);
		jo.put("command", command);
		jo.put("board", board.report());
		
		return jo;
		
	}
	
}
