package logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import replay.State;
import view.GUIView.RolitObserver;

public abstract class Game implements Replayable {
	protected boolean finished;
	protected List<Player> players;
	protected Board board;
	protected int currentPlayerIndex;
	private boolean exit;
	protected List<RolitObserver> observers;
	
	// Constructor de copia para generar los estados de las replays
	public Game(Game game) {
		this.finished = game.finished;
		this.players = game.players;
		
		this.board = new Board(game.board);
		
		this.currentPlayerIndex = game.currentPlayerIndex;
		this.exit = game.exit;
	}
	
	// Constructor de creaci�n a partir de carga
	public Game(Board board, List<Cube> list_cubes, List<Player> list_players, Color currentPlayerColor) {
		this.finished = false;
		this.board = board;
		this.exit = false;
		for (Cube c : list_cubes) {
			board.addCubeInPos(c);
		}
		this.players = list_players;
		int index = 0;
		boolean found = false;
		while (!found && index < players.size()) {
			if (players.get(index).getColor().equals(currentPlayerColor)) {
				found = true;
				this.currentPlayerIndex = index;
			}
			index++;
		}
		
		this.observers = new ArrayList<RolitObserver>();
	}
	
	public abstract void play(int x, int y) throws IllegalArgumentException;
	public abstract String toString();
	protected abstract Game copyMe();
	
	public void setExit() {
		this.exit = true;
	}

	public boolean exited() {
		return this.exit;
	}
	
	public boolean isFinished() {
		return this.finished || this.exit;
	}
		
	public Player getCurrentPlayer() {
		return this.players.get(currentPlayerIndex);
	}
	
	@Override
	public JSONObject report() {
		JSONObject gameJSONObject = new JSONObject();
		
		gameJSONObject.put("board", board.report());
		
		JSONArray playerJSONArray = new JSONArray();		 
		for (int i = 0; i < players.size(); ++i)
			playerJSONArray.put(players.get(i).report());
					
		gameJSONObject.put("players", playerJSONArray);
		
		gameJSONObject.put("turn", this.players.get(currentPlayerIndex).getColor().toString());
		
		return gameJSONObject;
	}

	public void addObserver(RolitObserver o) {
		this.observers.add(o);
		this.onRegister();
	}

	public void removeObserver(RolitObserver o) {
		this.observers.remove(o);
	}

	protected abstract void onTurnPlayed();	//Cada modo de juego debe tener su propia implementación
	protected abstract void onGameFinished();
	
	public void onStatusChange(String command) {
		for(RolitObserver o : observers) {
			o.onGameStatusChange(new State(command, this.getReplayable()));
		}
	}
	
	public void onStatusChange() {
		for(RolitObserver o : observers) {
			o.onGameStatusChange(new State(this.getReplayable()));
		}
	}

	protected void onRegister() {
		for(RolitObserver o : observers) {
			o.onRegister(new State(this.getReplayable()));
		}
	}

	protected void onError() {
		// TODO Auto-generated method stub
		
	}
	
	public Board getBoard() {
		return this.board;
	}
	
	public boolean[][] getShapeMatrix() {
		return this.board.getShapeMatrix();
	}
	
	public Replayable getReplayable() {
		return this.copyMe();
	}
	
	public List<Player> getPlayers() {
		return Collections.unmodifiableList(players);
	}

	public void updateGameFromServer(List<RolitObserver> observerList) {
		this.observers = observerList;
		this.onTurnPlayed();
	}

	public List<RolitObserver> getObserverList() {
		return Collections.unmodifiableList(this.observers);
	}
	
}
