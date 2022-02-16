package logic;

import java.util.ArrayList;
import java.util.List;

public class Game {
	private boolean finished;
	private List<Player> players;
	private Board board;
	private int currentPlayer;
	
	public Game(List<Player> players, Board board) {
		this.finished = false;
		this.players = players;
		this.board = board;
	}
	
	public void loadGame() {
		
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
