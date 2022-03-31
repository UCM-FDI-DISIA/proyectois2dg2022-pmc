package logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public abstract class Game implements Replayable {
	// TODO deberíamos hacer una clase de constantes gráficas que almacene todas estas cosas
	protected static final String RANKING = "RANKING DEL ROLIT";
	protected static final String MSG_POS = "En la posicion numero ";
	protected static final String MSG_REY = "QUIEN SERA EL REYYYYYY?????? :)";
	protected static final String MSG_GOOD_LUCK = "Suerte para la siguiente :)";
	
	protected boolean finished;
	protected List<Player> players;
	protected Board board;
	protected int currentPlayerIndex;
	private boolean exit;
	
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
	}
	
	public abstract boolean play(int x, int y);
	public abstract String toString();
	public abstract Game copyMe();
	public abstract String showRanking();
	
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
		
		gameJSONObject.put("turn", this.players.get(currentPlayerIndex).getColor());
		
		return gameJSONObject;
	}

}
