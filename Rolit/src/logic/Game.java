package logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Game {
	private boolean finished;
	private List<Player> players;
	private Board board;
	private int currentPlayer;
	private Map<Color, Player> colorPlayerMap;
	
	public Game(List<Player> players, Board board) {
		this.finished = false;
		this.players = players;
		this.board = board;
		this.colorPlayerMap = new HashMap<Color, Player>();
		for(Player player : players) {
			this.colorPlayerMap.put(player.getColor(), player);
		}
	}
	
	public void loadGame(List<Cube> cubes, List<Player> players, Color currentPlayerColor) {
		
	}
	
	public boolean play(int x, int y) {
		if(!tryToAddCube(x, y)) {
			System.out.println("La posición no es válida");
			return false;
		}
		Cube newCube = new Cube(x, y, players.get(currentPlayer).getColor());
		board.addCubeInPos(newCube);
		this.update(newCube);
		return true;
	}
	
	public void update(Cube newCube) {
		int posX = newCube.getX(), posY = newCube.getY();
		int newX, newY;
		int foundX = posX, foundY = posY;	//Las inicializo porque si no Eclipse se queja
		boolean found;
		Cube currentCube;
		
		//Hacemos dos bucles para indicar la direccion en la que vamos a buscar un cubo del color del jugador actual
		for(int dirX = -1; dirX <= 1; dirX++) {
			for(int dirY = -1; dirY <= 1; dirY++) {
				//Partiendo de la posicion actual (posX, posY), nos movemos en la direccion actual
				if(!(dirX == 0 && dirY == 0)) {
					newX = posX + dirX;
					newY = posY + dirY;
					found = false;
					while(isPositionInRange(newX, newY) && !found) {
						currentCube = getCubeInPos(newX, newY);
						if(currentCube != null) {
							if(currentCube.getColor().equals(players.get(currentPlayer).getColor())) {
								found = true;
								foundX = newX;
								foundY = newY;
							}
							else {
								newX += dirX;
								newY += dirY;
							}
						}
						else {
							newX += dirX;
							newY += dirY;
						}
					}
					if(found) {
						newX = posX + dirX;
						newY = posY + dirY;
						while(!(newX == foundX && newY == foundY))	{
							currentCube = getCubeInPos(newX, newY);
							if(currentCube != null) {
								//Quitamos un punto al jugador al cual quitamos un cubo de su color
								colorPlayerMap.get(currentCube.getColor()).addScore(-1);
								
								//Cambiamos de color el cubo en cuestion al color del jugador actual
								currentCube.setColor(players.get(currentPlayer).getColor());
								
								//Incrementamos en uno la puntuacion del jugador actual
								players.get(currentPlayer).addScore(1);
							}
							else {
								Cube addCube = new Cube(newX, newY, players.get(currentPlayer).getColor());
								board.addCubeInPos(addCube);
							}
							
							newX += dirX;
							newY += dirY;
						}
					}
				}
			}
		}
		//Cambiamos el turno
		currentPlayer = (currentPlayer + 1) % players.size();
	}
	
	public String positionToString(int x, int y) {
		Cube cube = getCubeInPos(x, y);
		if(cube == null) return "";
		else return cube.getColor().toString();
	}
	
	public boolean tryToAddCube(int x, int y) {
		boolean nearbyCube = false;
		if(!isPositionInRange(x, y) || getCubeInPos(x, y) != null) return false;
		for(int i = -1; i <= 1; i++) {
			for(int j = -1; j <= 1; j++) {
				if(isPositionInRange(i, j) && getCubeInPos(i, j) != null) {
					nearbyCube = true;
				}
			}
		}
		return nearbyCube;
	}
	
	public boolean isPositionInRange(int x, int y) {
		return x >= 0 && x < board.getSize() && y >= 0 && y < board.getSize();
	}
	
	public void showRanking() {
		GamePrinter.showRanking();
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
		return this.players;
	}
}
