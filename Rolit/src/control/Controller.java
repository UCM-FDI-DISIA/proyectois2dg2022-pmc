package control;

import Builders.GameBuilder;
import client.Client;

import javax.swing.SwingUtilities;

import commands.Command;

import org.json.JSONObject;

import logic.Game;
import replay.Replay;
import replay.State;
import view.GUIView.RolitObserver;

public class Controller {
	private Game game;
	private Replay replay;
	private Client clientRolit;
	private boolean onlineMode = false;
	
	public Controller() {
	}
	
	public State createGame(JSONObject o) {
		this.game = GameBuilder.createGame(o);
		this.replay = new Replay();
		return new State(game);
	}
	
	public void startReplay(Replay r) {
		r.startReplay();
	}
	
	public void addObserver(RolitObserver o) {
		game.addObserver(o);
	}
	
	public void executeCommand(String s) throws Exception {
		
		String[] parameters = s.toLowerCase().trim().split(" ");
		Command command = Command.getCommand(parameters);
		

		if (onlineMode) {
			
			if (game.getCurrentPlayer().getColor().equals(clientRolit.getPlayer().getColor())) {
				command.execute(game);
				clientRolit.updateGameToServer();
			}
			
		}
		else
			command.execute(game);
		
		replay.addState(s, game.getReplayable());
	}
	
	public void updateGameFromServer(JSONObject o) {
		Game newGame = GameBuilder.createGame(o);
		newGame.updateGameFromServer(game.getObserverList());
		game = newGame;
	}
	
	public State loadGame(String filePath) {
		game = GameBuilder.createGame(SaveLoadManager.loadGame(filePath));
		return new State(game);
	}
	
	public void setOnlineMode(boolean onlineMode) {
		this.onlineMode = onlineMode;
	}
	
	public void setClient(Client clientRolit) {
		this.clientRolit = clientRolit;
	}
	
	/*TODO private boolean askSaveReplay() {
		System.out.println(REPLAY_MSG);
		String ans = input.nextLine();
		return "y".equals(ans.toLowerCase());
	}*/
}