package model.logic;

import java.util.List;

import model.replay.GameState;
import utils.Pair;

/**
 * This class is in charge of managing who is the next Player and make it play
 * @author PMC
 *
 */
public class TurnManager {
	private List<Player> players;
	private int currentPlayer;
	
	/**
	 * Copy constructor (deep copy)
	 * @param t TurnManager to copy
	 */
	public TurnManager(TurnManager t) {
		if(t != null) {
			this.players = t.players;
			this.currentPlayer = t.currentPlayer;
		}
	}
	
	/**
	 * Constructor
	 * @param players List of players
	 * @param currentPlayer Current player index in the list 
	 */
	public TurnManager(List<Player> players, int currentPlayer) {
		this.players = players;
		this.currentPlayer = currentPlayer;
	}
	
	/**
	 * This method asks the current player to play, but it does not increase the turn
	 * @param state Current GameState
	 * @return Cube to place
	 */
	public Cube firstTurn(GameState state) {
		Pair<Integer, Integer> coords = players.get(currentPlayer).play(state);
		Cube newCube = null;
		if(coords != null) newCube = new Cube(coords.getFirst(), coords.getSecond(), players.get(currentPlayer));
		return newCube;
	}
	
	/**
	 * This method asks the current player to play
	 * @param state Current GameState
	 * @return Cube to place
	 */
	public Cube nextTurn(GameState state) {
		currentPlayer = (currentPlayer + 1) % players.size();
		Pair<Integer, Integer> coords = players.get(currentPlayer).play(state);
		Cube newCube = null;
		if(coords != null) newCube = new Cube(coords.getFirst(), coords.getSecond(), players.get(currentPlayer));
		return newCube;
	}
	
	/**
	 * It returns the current player index
	 * @return The current player index
	 */
	public int getCurrentPlayerIndex() {
		return this.currentPlayer;
	}
	
	public int getNextPlayerIndex() {
		return (this.currentPlayer + 1) % this.players.size();
	}
	
}
