package logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import utils.StringUtils;
import view.RolitObserver;

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
	public boolean play(int x, int y) {
		// Primero tenemos que comprobar que se pueda poner un cubo en la posicion
		// indicada
		if (!this.board.tryToAddCube(x, y)) {
			System.out.println("Not a valid position");
			this.onTurnPlayed();
			return false;
		}
		// En caso de poderse, ponemos el cubo en la posicion y actualizamos el tablero
		Cube newCube = new Cube(x, y, players.get(currentPlayerIndex));
		this.board.addCubeInPos(newCube);
		
		this.board.update(newCube);
		
		//Comprobamos si la partida termina con este turno
		this.finished = board.isBoardFull();
		
		this.state = copyMe(); //guardamos el estado del juego para que se pueda repetir partida
		
		// Cambiamos el turno al siguiente jugador en la lista si la partida no ha terminado
		if(!this.finished) currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
		
		this.onTurnPlayed();
		
		return true;
	}
	
	@Override
	public String showRanking() {
		// FIXME no tengo claro si se hace una copia o no ni tampoco de si importa esto
		List<Player> players = new ArrayList<Player>(this.players);
		Collections.sort(players);
		StringBuilder str = new StringBuilder(RANKING);
		
		str.append(StringUtils.LINE_SEPARATOR).append(MSG_REY).append(StringUtils.LINE_SEPARATOR);
		
		for (int i = 0; i < players.size(); ++i) {
			str.append(MSG_POS).append(i + 1).append(":").append(players.get(i).getName()).append(" " + players.get(i).getScore()).append(StringUtils.LINE_SEPARATOR);
		}
		
		str.append(MSG_GOOD_LUCK).append(StringUtils.LINE_SEPARATOR);
		
		return str.toString(); 
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
	public List<Rival> getRivals() {
		return Collections.unmodifiableList(this.players);
	}
}

