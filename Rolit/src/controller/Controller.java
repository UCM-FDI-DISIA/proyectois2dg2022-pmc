package controller;

import org.json.JSONObject;

import model.SaveLoadManager;
import model.builders.GameBuilder;
import model.commands.Command;
import model.logic.Game;
import model.online.Client;
import model.replay.GameState;
import model.replay.Replay;
import view.RolitObserver;

public class Controller {
	private volatile Game game;
	private volatile Client clientRolit;
	private boolean onlineMode = false;
	
	public Controller() {
		
	}
	
	public GameState createGame(JSONObject o) {
		this.game = GameBuilder.createGame(o);
		return new GameState(game);
	}
	
	public void startGame() {
		this.game.start();
	}
	
	public void startReplay(Replay r) {
		r.startReplay();
	}
	
	public void addObserver(RolitObserver o) {
		game.addObserver(o);
	}
	
	public void executeCommand(Command c) throws Exception {
		if (onlineMode) {
			if (game.getCurrentPlayer().getColor().equals(clientRolit.getPlayer().getColor())) {
				c.execute(game);
				while(!game.executedTurn()) {
					//No se hace nada para esperar a que el modelo haga lo que le haga falta
				}
				clientRolit.updateGameToServer();
			}			
		}
		else {	//FIXME hay que asegurar que el turno sea del jugador actual
			c.execute(game);
		}
	}
	
	public void updateGameFromServer(JSONObject o) {
		this.game.interrupt();
		Game newGame = GameBuilder.createGame(o);
		newGame.updateGameFromServer(game.getObserverList());		
		game = newGame;
		game.start();
	}
	
	public GameState loadGame(String filePath) {
		game = GameBuilder.createGame(SaveLoadManager.loadGame(filePath));
		return new GameState(game);
	}
	
	public void setOnlineMode(boolean onlineMode) {
		this.onlineMode = onlineMode;
	}
	
	public void setClient(Client clientRolit) {
		this.clientRolit = clientRolit;
	}
}