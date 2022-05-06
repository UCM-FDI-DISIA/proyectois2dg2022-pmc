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

public abstract class Game extends Thread implements Replayable {
	protected boolean finished;
	protected List<Player> players;
	protected Board board;
	private boolean exit;
	protected volatile List<RolitObserver> observers;
	protected volatile Queue<Cube> pendingCubes;
	protected Replay replay;
	protected GameState state;
	protected boolean executedTurn = false;
	protected TurnManager turnManager;
	
	// Constructor de copia para generar los estados de las replays
	public Game(Game game) {
		this.finished = game.finished;
		this.players = game.players;		
		this.board = new Board(game.board);		
		this.exit = game.exit;
		this.turnManager = new TurnManager(game.turnManager);
		
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
		while (index < players.size()) {
			if (players.get(index).getColor().equals(currentPlayerColor)) {
				break;
			}
			index++;
		}
		
		this.observers = new ArrayList<RolitObserver>();
		// FIXME esto tiene que dar problemas a la hora de cargar seguro
		this.pendingCubes = new ArrayDeque<>();
		this.turnManager = new TurnManager(list_players, index);
		this.replay = new Replay();
		this.state = new GameState(this);
	}
	
	@Override
	public void run() {
		// FIXME en la depuración esto no llega a terminar nunca
		Cube nextCube;
		nextCube = this.turnManager.firstTurn(new GameState(copyMe()));//FIXME Se crea tambien en el onTurnPlayed
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
	
	// Este es el método que realmente sirve para hacer lo que sería un turno completo
	public abstract void play() throws IllegalArgumentException;
	public abstract String toString();
	public abstract Game copyMe();
	
	// FIXME tiene que haber una forma de restringir esto seguro
	public void setExit() {
		this.exit = true;
		this.onGameExited();
	}

	public boolean exited() {
		return this.exit;
	}
	
	public boolean isFinished() {
		return this.finished || this.exit;
	}
	
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

	public void addObserver(RolitObserver o) {
		this.observers.add(o);
		// FIXME sospecho que solo sería necesario llamar al onRegister del que se acaba de registrar
		this.onRegister();
	}
	
	protected abstract void onTurnPlayed();	// Cada modo de juego debe tener su propia implementación
	protected abstract void onGameFinished();
	
	public void onStatusChange(String command) {
		for(RolitObserver o : observers) {
			o.onGameStatusChange(new GameState(command, this.copyMe()));
		}
	}

	protected void onRegister() {
		for(RolitObserver o : observers) {
			o.onRegister(new GameState(this.copyMe()));
		}
	}
	
	public void onGameExited() {
		for(RolitObserver o : observers) {
			o.onGameExited(replay);
		}
	}

	protected void onError(String msg) {
		for(RolitObserver o : observers) {
			o.onError(msg);
		}
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
