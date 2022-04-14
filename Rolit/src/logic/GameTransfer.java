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
	private boolean puedeJugar;
	private Client clientRolit;
	
	public GameTransfer(Client clientRolit) {
		this.clientRolit = clientRolit;
		this.puedeJugar = false;
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
		if (puedeJugar) {	
			command.execute(game);
			clientRolit.updateGameToServer();
			puedeJugar = false;
		}
		
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
			Player turno = game.getCurrentPlayer();
			if (turno.equals(clientRolit.getPlayer()))
				puedeJugar = true;
		}
		else
			game.updateGameFromServer(GameBuilder.createGame(JSONJuegoNuevo));
		
	}
	
}
