package logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import utils.StringUtils;
import view.GUIView.RolitObserver;

public class GameClassic extends Game {
	
	// Constructor de copia para generar los estados de las replays
	public GameClassic(GameClassic game) {
		super(game);
	}
	
	// Constructor de creaci�n a partir de carga
	public GameClassic(Board board, List<Cube> list_cubes, List<Player> list_players, Color currentPlayerColor) {
		super(board, list_cubes, list_players, currentPlayerColor);
	}
	
	@Override
	public Game copyMe() {
		return new GameClassic(this);
	}
	
	@Override
	public void play(int x, int y) throws IllegalArgumentException {		
		// En caso de poderse, ponemos el cubo en la posicion y actualizamos el tablero
		Cube newCube = new Cube(x, y, players.get(currentPlayerIndex));
		this.board.addCubeInPos(newCube);
		
		this.board.update(newCube);
		
		//Comprobamos si la partida termina con este turno
		this.finished = board.isBoardFull();
		if (this.finished)
			this.onGameFinished();
		
		this.state = copyMe(); //guardamos el estado del juego para que se pueda repetir partida
		
		// Cambiamos el turno al siguiente jugador en la lista si la partida no ha terminado
		if(!this.finished)
			currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
	}

	@Override
	public String toString() {
		StringBuilder bf = new StringBuilder();
		bf.append("Turno: ");
		bf.append(getCurrentPlayer().toString());
		bf.append(StringUtils.LINE_SEPARATOR);
		bf.append(board.toString());
		return bf.toString();
	}
	
	@Override
	public JSONObject report() {
		JSONObject gameJSONObject = super.report();
		gameJSONObject.put("type", "GameClassic");
		return gameJSONObject;
	}
	
	@Override
	public void onTurnPlayed() {
		for(RolitObserver o : observers) {
			o.onTurnPlayed(players.get(currentPlayerIndex).getName(), players.get(currentPlayerIndex).getColor());
		}
	}

	@Override
	protected void onGameFinished() {
		for (RolitObserver o : this.observers)
			o.onGameFinished(this.players, "Players");		
	}
	
}

