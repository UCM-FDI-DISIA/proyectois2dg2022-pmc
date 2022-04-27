package logic;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;

import org.json.JSONArray;
import org.json.JSONObject;

import replay.GameState;
import view.GUIView.RolitObserver;

public abstract class Game extends Thread implements Replayable {
	protected boolean finished;
	protected List<Player> players;
	protected Board board;
	protected int currentPlayerIndex;
	private boolean exit;
	protected volatile List<RolitObserver> observers;
	protected volatile Queue<Cube> pendingCubes;
	protected boolean executedTurn = false;
	
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
		// FIXME esto tiene que dar problemas a la hora de cargar seguro
		this.pendingCubes = new ArrayDeque<>();
	}
	
	@Override
	public void run() {
		this.onFirstPlay();
		// FIXME en la depuración esto no llega a terminar nunca
		while (!this.finished && !this.exit && !Thread.interrupted()) {
			this.play();
		}		
		// FIXME mostrar el ranking
	}
	
	// Este es el método que realmente sirve para hacer lo que sería un turno completo
	public abstract void play() throws IllegalArgumentException;
	public abstract String toString();
	public abstract Game copyMe();
	
	// FIXME tiene que haber una forma de restringir esto seguro
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
		// FIXME sospecho que solo sería necesario llamar al onRegister del que se acaba de registrar
		this.onRegister();
	}
	
	public void removeObserver(RolitObserver o) {
		this.observers.remove(o);
	}

	protected abstract void onFirstPlay();
	protected abstract void onTurnPlayed();	// Cada modo de juego debe tener su propia implementación
	protected abstract void onGameFinished();
	
	public void onStatusChange(String command) {
		for(RolitObserver o : observers) {
			o.onGameStatusChange(new GameState(command, this.copyMe()));
		}
	}
	
	public void onStatusChange() {
		for(RolitObserver o : observers) {
			o.onGameStatusChange(new GameState(this.copyMe()));
		}
	}

	protected void onRegister() {
		for(RolitObserver o : observers) {
			o.onRegister(new GameState(this.copyMe()));
		}
	}

	protected void onError() {
		// TODO Auto-generated method stub
	}

	public void updateGameFromServer(List<RolitObserver> observerList) {
		this.observers = observerList;
		this.onTurnPlayed();
	}

	public List<RolitObserver> getObserverList() {
		return Collections.unmodifiableList(this.observers);
	}
	
	public void addCubeToQueue(Cube c) {
		this.pendingCubes.add(c);
	}
	
	public boolean executedTurn() {
		if(!this.executedTurn) return false;
		else {
			this.executedTurn = false;
			return true;
		}
	}

}
