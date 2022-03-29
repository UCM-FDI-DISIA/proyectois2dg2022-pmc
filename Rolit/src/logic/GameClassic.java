package logic;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import utils.StringUtils;

// FIXME MIRAR SI ES VIABLE EL GAME ABSTRACTO
public class GameClassic extends Game {
	
	// Constructor de copia para generar los estados de las replays
	public GameClassic(GameClassic game) {
		super(game);
	}
	
	// Constructor de creación a partir de carga
	public GameClassic(Board board, List<Cube> list_cubes, List<Player> list_players, Color currentPlayerColor) {
		super(board, list_cubes, list_players, currentPlayerColor);
	}
	
	@Override
	public Game copyMe() {
		return new GameClassic(this);
	}
	
	@Override
	public boolean play(int x, int y) {
		// Primero tenemos que comprobar que se pueda poner un cubo en la posicion
		// indicada
		if (!this.board.tryToAddCube(x, y)) {
			System.out.println("Not a valid position");
			return false;
		}
		// En caso de poderse, ponemos el cubo en la posicion y actualizamos el tablero
		Cube newCube = new Cube(x, y, players.get(currentPlayerIndex));
		this.board.addCubeInPos(newCube);
		
		this.board.update(newCube);
		
		//Comprobamos si la partida termina con este turno
		this.finished = board.isBoardFull();
		
		// Cambiamos el turno al siguiente jugador en la lista si la partida no ha terminado
		if(!this.finished) currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
			return true;
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
}

