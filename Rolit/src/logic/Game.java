package logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import view.RolitObserver;

public abstract class Game implements Replayable {	
	protected boolean finished;
	protected List<Player> players;
	protected Board board;
	protected int currentPlayerIndex;
	private boolean exit;
	private List<RolitObserver> observers;
	
	// Constructor de copia para generar los estados de las replays
	public Game(Game game) {
		this.finished = game.finished;
		this.players = game.players;
		
		this.board = new Board(game.board);
		
		this.currentPlayerIndex = game.currentPlayerIndex;
		this.exit = game.exit;
		this.observers = new ArrayList<RolitObserver>();
		
		this.onGameCreated();
	}
	
	// Constructor de creaci�n a partir de carga
	public Game(Board board, List<Cube> list_cubes, List<Player> list_players, Color currentPlayerColor) {
		this.finished = false;
		this.board = board;
		this.players = new ArrayList<Player>();
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
	
	public abstract boolean play(int x, int y);
	public abstract String toString();
	public abstract Game copyMe();
	
	public void setExit() {
		this.exit = true;
	}

	public boolean exited() {
		return this.exit;
	}
	
	public boolean isFinished() {
		return this.finished || this.exit;
	}

	public List<Player> getPlayers() {
		return Collections.unmodifiableList(this.players);
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
		
		gameJSONObject.put("turn", this.players.get(currentPlayerIndex).getColor());
		
		return gameJSONObject;
	}

	public void addObserver(RolitObserver o) {
		this.observers.add(o);
	}

	public void removeObserver(RolitObserver o) {
		this.observers.remove(o);
	}
	
	public void onBoardAdded() {
		for(RolitObserver o : observers) {
			o.onBoardCreated(board);
		}
	}

	public void onGameCreated() {
		for(RolitObserver o : observers) {
			o.onGameCreated(this, this.board);
		}
	}

	public void onTurnPlayed() {
		for(RolitObserver o : observers) {
			o.onTurnPlayed();
		}
	}

	public void onCommandIntroduced() {
		for(RolitObserver o : observers) {
			o.onCommandIntroduced(this, this.board, null);
		}
	}

	public void onReplayLeftButton() {
		for(RolitObserver o : observers) {
			o.onReplayLeftButton(this, this.board);
		}
	}

	public void onReplayRightButton() {
		for(RolitObserver o : observers) {
			o.onReplayRightButton(this, this.board);
		}
	}

	public void onRegister() {
		for(RolitObserver o : observers) {
			o.onRegister(this, this.board, null);
		}
	}

	public void onError() {
		// TODO Auto-generated method stub
		
	}
	
	public Board getBoard() {
		
		return this.board;
		
	}
}
