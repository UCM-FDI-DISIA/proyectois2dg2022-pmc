package logic;

import java.util.List;

import org.json.JSONObject;

import Builders.GameBuilder;
import Rolit.ClientRolit;
import commands.Command;
import control.SaveLoadManager;
import view.BoardGUI;
import view.RolitObserver;

public class GameTransfer {
	
	private Game game;
	private ClientRolit clientRolit;
	
	public GameTransfer(ClientRolit clientRolit) {
		this.clientRolit = clientRolit;
	}

	public void onFirstPlay() {
		game.onFirstPlay();
		
	}

	public void loadGame(String path) {
		game = SaveLoadManager.loadGame(path);
		
	}

	public void addObserver(RolitObserver o) {
		game.addObserver(o);
		
	}

	public boolean[][] getShapeMatrix() {
		return game.getShapeMatrix();
	}

	public Cube getCubeInPos(int x, int y) {
		// TODO Auto-generated method stub
		return game.getBoard().getCubeInPos(x, y);
	}

	public List<Rival> getRivals() {
		// TODO Auto-generated method stub
		return game.getRivals();
	}

	public void executeCommand(Command command) {
		command.execute(game);
		clientRolit.updateGameToServer();
	}

	public void createGame(JSONObject createJSONObjectGame) {
		game = GameBuilder.createGame(createJSONObjectGame);
		
	}

	public JSONObject getGameReport() {
		return game.report();
		
	}

	public void updateGame(JSONObject JSONJuegoNuevo) {
		game = GameBuilder.createGame(JSONJuegoNuevo);
		
	}

}
