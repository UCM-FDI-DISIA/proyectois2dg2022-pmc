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
	Replayable turn;
	Replayable command;
	Replayable board;
	
	State(Replayable turn, Replayable command, Replayable board){
		this.turn = turn;
		this.command = command;
		this.board = board;
	}
	
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append("Turn: ").append(turn).append(StringUtils.LINE_SEPARATOR);
		str.append("Command > ").append(command).append(StringUtils.LINE_SEPARATOR);
		str.append(board);
		return str.toString();
	}
	
	public JSONObject report() {
		JSONObject jo = new JSONObject();
		
		jo.put("turn", turn.report());
		jo.put("command", command.report());
		jo.put("board", board.report());
		
		return jo;
		
	}
	
}
