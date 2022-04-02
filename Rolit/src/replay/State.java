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

public class State implements Reportable{
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
	
	public static void main(String[] args) {
		Board b = new Board(Shape.SM);
		Player p1 = new Player(Color.YELLOW, "Juandi");
		Player p2 = new Player(Color.BROWN, "Leo");
		Player p3 = new Player(Color.RED, "dani");
		List<Player> lp = new ArrayList<Player>();
		lp.add(p1);
		lp.add(p2);
		lp.add(p3);
		List<Cube> lc = new ArrayList<Cube>();
		Cube c = new Cube(3, 4, p2);
		Cube c2 = new Cube(3, 5, p1);
		lc.add(c);
		lc.add(c2);
		Game game = new GameClassic(b, lc, lp, Color.YELLOW);
		game.play(3, 6);
		State state = new State("p", game);
		
		System.out.print(state.report());
	}
	
}
