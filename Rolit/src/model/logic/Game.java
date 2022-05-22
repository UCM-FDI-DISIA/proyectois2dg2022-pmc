package model.logic;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import org.json.JSONArray;
import org.json.JSONObject;
import model.replay.GameState;
import model.replay.Replay;
import view.RolitObserver;

/**
 * This class represents a Rolit Game and is in charge of managing each player turn.
 * It is also responsible for generating a game replay, adding each GameState to a Replay.
 * @author PMC
 *
 */
public abstract class Game extends Thread implements Replayable {
	protected boolean finished;
	private boolean exit;
	/**
	 * It becomes true after a turn has finished its execution
	 */
	protected boolean executedTurn = false;

	/**
	 * List of players
	 */
	protected List<Player> players;
	
	/**
	 * Board of the game
	 */
	protected Board board;
	
	/**
	 * List of view observers
	 */
	protected volatile List<RolitObserver> observers;
	
	/**
	 * List of pending cubes to add to the board
	 */
	protected volatile Queue<Cube> pendingCubes;
	
	/**
	 * Replay of the game
	 */
	protected Replay replay;
	
	/**
	 * Current state of the game
	 */
	protected GameState state;
	
	/**
	 * TurnManager of the game
	 */
	protected TurnManager turnManager;
	
	/**
	 * Copy constructor (deep copy)
	 * @param game Game to copy
	 */
	public Game(Game game) {
		this.finished = game.finished;
		this.players = game.players;		
		this.board = new Board(game.board);		
		this.exit = game.exit;
		this.turnManager = new TurnManager(game.turnManager);
	}
	
	/**
	 * Constructor
	 * @param board Game board
	 * @param list_cubes List with the cubes that are already in the board
	 * @param list_players List with the players
	 * @param currentPlayerColor First turn player color
	 */
	public Game(Board board, List<Cube> list_cubes, List<Player> list_players, Color currentPlayerColor) {
		this.finished = false;
		this.board = board;
		this.exit = false;
		for (Cube c : list_cubes) {
			board.addCubeInPos(c);
		}
		this.players = list_players;
		int index = 0;
		while (index < players.size()) {
			if (players.get(index).getColor().equals(currentPlayerColor)) {
				break;
			}
			index++;
		}
		
		this.observers = new ArrayList<RolitObserver>();
		this.pendingCubes = new ArrayDeque<>();
		this.turnManager = new TurnManager(list_players, index);
		this.replay = new Replay();
		this.state = new GameState(this);
	}
	
	/**
	 * This methods starts the execution of the game
	 */
	@Override
	public void run() {
		Cube nextCube;
		nextCube = this.turnManager.firstTurn(new GameState(copyMe()));
		if(nextCube != null) this.addCubeToQueue(nextCube);
		while (!this.finished && !this.exit && !Thread.interrupted()) {
			try {
				this.play();
			}
			catch (IllegalArgumentException e) {
				this.onError(e.getMessage());
				this.executedTurn = true;
			}
		}
	}
	
	/**
	 * This method manages a player turn
	 * @throws IllegalArgumentException This exception is thrown if the cube the player is trying to add is not in a valid position
	 */
	public abstract void play() throws IllegalArgumentException;
	
	public abstract String toString();
	
	/**
	 * This method returns a deep copy of the Game
	 * @return A deep copy of the Game
	 */
	public abstract Game copyMe();
	
	/**
	 * This method exits the game
	 */
	public void exit() {
		this.exit = true;
		this.onGameExited();
	}

	/**
	 * It checks whether the game was exit manually
	 * @return true if the user used the exit command or false otherwise
	 */
	public boolean exited() {
		return this.exit;
	}
	
	/**
	 * It returns whether the game is finished or not.
	 * @return true is the game is finished and false otherwise
	 */
	public boolean isFinished() {
		return this.finished || this.exit;
	}
	
	/**
	 * This method returns the player that is playing
	 * @return Player that is playing
	 */
	public Player getCurrentPlayer() {
		return this.players.get(turnManager.getCurrentPlayerIndex());
	}
	
	@Override
	public JSONObject report() {
		JSONObject gameJSONObject = new JSONObject();
		
		gameJSONObject.put("board", board.report());
		
		JSONArray playerJSONArray = new JSONArray();		 
		for (int i = 0; i < players.size(); ++i)
			playerJSONArray.put(players.get(i).report());
		
		gameJSONObject.put("players", playerJSONArray);
		
		gameJSONObject.put("turn", this.players.get(turnManager.getCurrentPlayerIndex()).getColor().toString());
		
		return gameJSONObject;
	}
	
	/**
	 * It adds a RolitObserver to the list of observers
	 * @param o Observer to add
	 */
	public void addObserver(RolitObserver o) {
		this.observers.add(o);
		this.onRegister();
	}
	
	/**
	 * It notifies onTurnedPlayed() to every observer
	 */
	protected abstract void onTurnPlayed();	// Each game mode needs to have its own implementation
	
	/**
	 * It notifies onGameFinished() to every observer
	 */
	protected abstract void onGameFinished();
	
	/**
	 * It notifies onStatusChange() to every observer
	 * @param command Name of the command that generated the change
	 */
	public void onStatusChange(String command) {
		for(RolitObserver o : observers) {
			o.onGameStatusChange(new GameState(command, this.copyMe()));
		}
	}

	/**
	 * It notifies onRegister() to every observer
	 */
	protected void onRegister() {
		for(RolitObserver o : observers) {
			o.onRegister(new GameState(this.copyMe()));
		}
	}
	
	/**
	 * It notifies onGameExited() to every observer
	 */
	public void onGameExited() {
		for(RolitObserver o : observers) {
			o.onGameExited(replay);
		}
	}

	/**
	 * It notifies onError() to every observer
	 * 
	 * @param msg Error message
	 */
	protected void onError(String msg) {
		for(RolitObserver o : observers) {
			o.onError(msg);
		}
	}

	/**
	 * It updates the observer list of the game.
	 * @param observerList List of observers
	 */
	public void updateGameFromServer(List<RolitObserver> observerList) {
		this.observers = observerList;
		this.onTurnPlayed();
	}

	/**
	 * It returns an unmodifiable list of observers
	 * @return An unmodifiable list of observers
	 */
	public List<RolitObserver> getObserverList() {
		return Collections.unmodifiableList(this.observers);
	}
	
	/**
	 * This method adds the cube c to the queue of cubes. It will be placed as soon as possible.
	 * @param c Cube to add
	 */
	public void addCubeToQueue(Cube c) {
		this.pendingCubes.add(c);
	}
	
	/**
	 * It returns whether last turn was executed or not
	 * @return true if the last turn was executed and false otherwise
	 */
	public boolean executedTurn() {
		if(!this.executedTurn) return false;
		else {
			this.executedTurn = false;
			return true;
		}
	}
	
	
}
