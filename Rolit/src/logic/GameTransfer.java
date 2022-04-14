package logic;

import java.util.List;

import org.json.JSONObject;

import Builders.GameBuilder;
import client.Client;
import commands.Command;
import control.SaveLoadManager;
import view.BoardGUI;
import view.RolitObserver;

public class GameTransfer {
	
	private Game game;
	private Client clientRolit;
	private boolean onlineMode;
	
	public GameTransfer(Client clientRolit, boolean onlineMode) {
		this.clientRolit = clientRolit;
	}

	public GameTransfer() { //juego señuelo que crea el servidor para poder trabajar con el primer report
		
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
		if (onlineMode) {
			if (game.getCurrentPlayer().getColor().equals(clientRolit.getPlayer().getColor())) {
				command.execute(game);
				clientRolit.updateGameToServer();
			}
		}
		else
			command.execute(game);
		
	}

	public void createGame(JSONObject createJSONObjectGame) {
		game = GameBuilder.createGame(createJSONObjectGame);
		
	}

	public JSONObject getGameReport() {
		return game.report();
		
	}

	public void updateGameFromServer(JSONObject JSONJuegoNuevo) {
		if (game == null) {
			game = GameBuilder.createGame(JSONJuegoNuevo);
			
		}
		else {
			game.updateGameFromServer(GameBuilder.createGame(JSONJuegoNuevo));
		}
			
		
	}
	
}
