package logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import utils.StringUtils;

public class Game implements Replayable{
	private boolean finished;
	private List<Player> players;
	private Board board;
	private int currentPlayerIndex;
	private boolean exit;
	private static final String AVAILABLE_COLORS_MSG = "Available colors:";

	public Game(Board board) {
		this.finished = false;
		this.board = board;
		this.players = new ArrayList<Player>();
		this.exit = false;
	}
	
	public Game(Game game) {
		this.finished = game.finished;
		this.players = game.players;
		this.board = new Board(game.board);
		this.currentPlayerIndex = game.currentPlayerIndex;
		this.exit = game.exit;
	}
	
	public Game(Board board, List<Cube> list_cubes, List<Player> list_players, Color currentPlayerColor) {
		this(board);
		this.loadGame(list_cubes, list_players, currentPlayerColor);
	}

	public void loadGame(List<Cube> cubes, List<Player> players, Color currentPlayerColor) {
		for (Cube c : cubes) {
			board.addCubeInPos(c);
		}
		this.players = players;
		int index = 0;
		boolean found = false;
		while (!found && index < players.size()) {
			if (players.get(index).getColor().equals(currentPlayerColor)) {
				found = true;
				currentPlayerIndex = index;
			}
			index++;
		}
	}

	public List<Cube> saveBoard() {
		List<Cube> cubeList = new ArrayList<Cube>();
		for (int i = 0; i < board.getSize(); i++) {
			for (int j = 0; j < board.getSize(); j++) {
				if(board.getCubeInPos(i, j) != null)
					cubeList.add(board.getCubeInPos(i, j));
			}
		}
		return Collections.unmodifiableList(cubeList);
	}

	public void tryToAddPlayer(String name, char c) {
		Color color = Color.valueOfIgnoreCase(c);
		if(color == null) 
			throw new IllegalArgumentException("The shortcut does not match any color");
		Player player = Player.getPlayer(color);
		if(player == null) {
			players.add(new Player(color, name));
		}
		else {
			throw new IllegalArgumentException("The selected color is not available");
		}
	}
	
	public String availableColors() {
		StringBuilder str = new StringBuilder();
		str.append(String.format("%s%n", AVAILABLE_COLORS_MSG));
		for(Color c : Color.values()) {
			if(Player.getPlayer(c) == null)
				str.append(String.format("%s: %s%n", c, c.name()));
		}
		return str.toString();
	}
	
	public boolean play(int x, int y) {
		// Primero tenemos que comprobar que se pueda poner un cubo en la posicion
		// indicada
		if (!board.tryToAddCube(x, y)) {
			System.out.println("Not a valid position");
			return false;
		}
		// En caso de poderse, ponemos el cubo en la posicion y actualizamos el tablero
		Cube newCube = new Cube(x, y, players.get(currentPlayerIndex));
		board.addCubeInPos(newCube);
		
		board.update(newCube);
		
		//Comprobamos si la partida termina con este turno
		finished = board.isBoardFull();
		
		// Cambiamos el turno al siguiente jugador en la lista si la partida no ha terminado
		if(!finished) currentPlayerIndex = (currentPlayerIndex + 1) % players.size();		return true;
	}

	public String positionToString(int x, int y) {
		Cube cube = getCubeInPos(x, y);
		if (cube == null)
			return " ";
		else
			return cube.getColor().toString();
	}

	public void setExit() {
		this.exit = true;
	}

	public Cube getCubeInPos(int x, int y) {
		return board.getCubeInPos(x, y);
	}

	public int getBoardSize() {
		return board.getSize();
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
	
	public Color getCurrentColor() {
		return this.players.get(currentPlayerIndex).getColor();
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
		JSONObject gameJSONObject = new JSONObject();
		
		gameJSONObject.put("board", board.report());
		
		JSONArray playerJSONArray = new JSONArray();		 
		for (int i = 0; i < players.size(); ++i)
			playerJSONArray.put(players.get(i).report());
					
		gameJSONObject.put("players", playerJSONArray);
		
		gameJSONObject.put("turn", this.getCurrentColor());
		
		return gameJSONObject;
	}
}

