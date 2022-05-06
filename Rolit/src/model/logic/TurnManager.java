package model.logic;

import java.util.List;

import model.replay.GameState;
import utils.Pair;

public class TurnManager {
	private List<Player> players;
	private int currentPlayer;
	
	//Constructor de copia
	public TurnManager(TurnManager t) {
		if(t != null) {
			this.players = t.players;
			this.currentPlayer = t.currentPlayer;
		}
	}
	
	public TurnManager(List<Player> players, int currentPlayer) {
		this.players = players;
		this.currentPlayer = currentPlayer;
	}
	
	public Cube firstTurn(GameState state) {
		Pair<Integer, Integer> coords = players.get(currentPlayer).play(state);
		Cube newCube = null;
		if(coords != null) newCube = new Cube(coords.getFirst(), coords.getSecond(), players.get(currentPlayer));
		return newCube;
	}
	
	public Cube nextTurn(GameState state) {
		currentPlayer = (currentPlayer + 1) % players.size();
		Pair<Integer, Integer> coords = players.get(currentPlayer).play(state);
		Cube newCube = null;
		if(coords != null) newCube = new Cube(coords.getFirst(), coords.getSecond(), players.get(currentPlayer));
		return newCube;
	}
	
	public int getCurrentPlayerIndex() {
		return this.currentPlayer;
	}
	
}
