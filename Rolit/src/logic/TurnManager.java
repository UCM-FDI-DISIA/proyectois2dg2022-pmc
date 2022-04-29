package logic;

import java.util.List;

import replay.GameState;
import utils.Pair;

public class TurnManager {
	private List<Player> players;
	private int currentPlayer;
	
	public TurnManager(List<Player> players) {
		this(players, 0);
	}
	
	public TurnManager(List<Player> players, int currentPlayer) {
		this.players = players;
		this.currentPlayer = currentPlayer;
	}
	
	public Cube firstTurn(GameState state) {
		return this.nextTurn(state);
	}
	
	public Cube nextTurn(GameState state) {
		Pair<Integer, Integer> coords = players.get(currentPlayer).play(state);
		Cube newCube = null;
		if(coords != null) newCube = new Cube(coords.getFirst(), coords.getSecond(), players.get(currentPlayer));
		currentPlayer = (currentPlayer + 1) % players.size();
		return newCube;
	}
	
	public int getCurrentPlayerIndex() {
		return this.currentPlayer;
	}
	
}
