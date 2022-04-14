package control;

import Builders.GameBuilder;
import commands.Command;

import org.json.JSONObject;

import logic.Game;
import replay.Replay;
import view.GUIView.RolitObserver;

public class Controller {
	private Game game;
	private Replay replay;
	
	
	public void createGame(JSONObject o) {
		this.game = GameBuilder.createGame(o);
	}
	
	public void startReplay(Replay r) {
		r.startReplay();
	}
	
	public void addObserver(RolitObserver o) {
		game.addObserver(o);
	}
	
	public void executeCommand(String s) throws Exception {
		String[] parameters = s.toLowerCase().trim().split(" ");
		Command command = null;
		command = Command.getCommand(parameters);
		command.execute(game);
		replay.addState(s, game.getState());
	}
	
	private void printGame() {
		System.out.println(this.game.toString());
	}
	
	/*private boolean askSaveReplay() {
		System.out.println(REPLAY_MSG);
		String ans = input.nextLine();
		return "y".equals(ans.toLowerCase());
	}*/
}