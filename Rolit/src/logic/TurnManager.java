package logic;

import java.util.List;

import replay.GameState;
import utils.Pair;

public class TurnManager {
	private List<Player> players;
	private int currentPlayer;
	
	public TurnManager(List<Player> players) {
		this.players = players;
		this.currentPlayer = 0;
	}
	
	public TurnManager(List<Player> players, int currentPlayer) {
		this.players = players;
		this.currentPlayer = currentPlayer;
	}
	
	public Cube nextTurn(GameState state) {
		Pair<Integer, Integer> coords = players.get((currentPlayer + 1) % players.size()).play(state);
		Cube newCube = null;
		if(coords != null) newCube = new Cube(coords.getFirst(), coords.getSecond(), players.get(currentPlayer));
		currentPlayer++;
		return newCube;
	}
	
}
