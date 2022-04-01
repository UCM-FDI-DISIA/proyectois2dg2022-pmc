package logic;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import utils.StringUtils;

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
	
	public static void main(String[] args) {
		Board b = new Board(Shape.DS);
		Player p1 = new Player(Color.BEIGE, "Juandi");
		Player p2 = new Player(Color.BLACK, "Leo");
		Player p3 = new Player(Color.YELLOW, "dani");
		List<Player> lp = new ArrayList<Player>();
		lp.add(p1);
		lp.add(p2);
		lp.add(p3);
		List<Cube> lc = new ArrayList<Cube>();
		Cube c = new Cube(3, 4, p2);
		Cube c2 = new Cube(3, 5, p1);
		lc.add(c);
		lc.add(c2);
		Game game = new GameClassic(b, lc, lp, Color.BEIGE);
		game.play(3,6);
		
		System.out.print(game.report());
	}
}

