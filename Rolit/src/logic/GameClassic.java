package logic;

import java.util.List;

import org.json.JSONObject;

import replay.GameState;
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
	public void play() throws IllegalArgumentException {
		if (!this.pendingCubes.isEmpty()) {
			// FIXME no puede ser la mejor forma de hacerlo
			Cube c = this.pendingCubes.poll();
			
			// En caso de poderse, ponemos el cubo en la posicion y actualizamos el tablero
			Cube newCube = new Cube(c.getX(), c.getY(), players.get(currentPlayerIndex));
			this.board.addCubeInPos(newCube);
			
			this.board.update(newCube);
			
			//Comprobamos si la partida termina con este turno
			this.finished = board.isBoardFull();
			if (this.finished)
				this.onGameFinished();
			
			// Cambiamos el turno al siguiente jugador en la lista si la partida no ha terminado
			if(!this.finished) {
				currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
				onTurnPlayed();
			}			
			this.executedTurn = true;
		}		
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
	protected void onFirstPlay() {
		GameState state = new GameState(this.copyMe());
		for(RolitObserver o : observers) {
			o.onFirstPlay(state);
		}
	}
	
	@Override
	public void onTurnPlayed() {
		GameState state = new GameState(this.copyMe());
		for(RolitObserver o : observers) {
			o.onTurnPlayed(state);
		}
	}

	@Override
	protected void onGameFinished() {
		for (RolitObserver o : this.observers)
			o.onGameFinished(this.players, "Players");		
	}
	
}

