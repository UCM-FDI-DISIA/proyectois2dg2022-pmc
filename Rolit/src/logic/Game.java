package logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Game implements Saveable{
	private boolean finished;
	private List<Player> players;
	private Board board;
	private int currentPlayerIndex;
	private Map<Color, Player> colorPlayerMap;

	public Game(List<Player> players, Board board) {
		this.finished = false;
		this.players = players;
		this.board = board;
		this.colorPlayerMap = new HashMap<Color, Player>();
		for (Player player : players) {
			this.colorPlayerMap.put(player.getColor(), player);
		}
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

	public boolean play(int x, int y) {
		// Primero tenemos que comprobar que se pueda poner un cubo en la posicion
		// indicada
		if (!tryToAddCube(x, y)) {
			System.out.println("La posicion no es valida");
			return false;
		}
		// En caso de poderse, ponemos el cubo en la posicion y actualizamos el tablero
		Cube newCube = new Cube(x, y, players.get(currentPlayerIndex).getColor());
		board.addCubeInPos(newCube);
		this.update(newCube);
		return true;
	}

	public void update(Cube newCube) {
		board.update(newCube);
		//Comprobamos si la partida termina con este turno
		finished = board.isBoardFull();
		
		// Cambiamos el turno al siguiente jugador en la lista si la partida no ha terminado
		if(!finished) currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
	}

	public String positionToString(int x, int y) {
		Cube cube = getCubeInPos(x, y);
		if (cube == null)
			return " ";
		else
			return cube.getColor().toString();
	}

	public boolean tryToAddCube(int x, int y) {
		if (this.board.getNumCubes() > 0) {
			boolean nearbyCube = false;
			if (!isPositionInRange(x, y) || getCubeInPos(x, y) != null)
				return false;
			for (int i = -1; i <= 1; i++) {
				for (int j = -1; j <= 1; j++) {
					if (isPositionInRange(x + i, y + j) && getCubeInPos(x + i, y + j) != null) {
						nearbyCube = true;
					}
				}
			}
			return nearbyCube;
		} else
			return isPositionInRange(x, y);
	}

	public void flipTrapped() {

	}

	public Cube getCubeInPos(int x, int y) {
		return board.getCubeInPos(x, y);
	}

	public int getBoardSize() {
		return board.getSize();
	}

	public boolean isFinished() {
		return this.finished;
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
}
