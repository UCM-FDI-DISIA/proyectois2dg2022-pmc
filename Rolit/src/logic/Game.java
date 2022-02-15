package logic;

import java.util.ArrayList;
import java.util.List;

public class Game {
	private boolean finished;
	private List<Player> players;
	private List<Cube> cubes;
	private int currentPlayer;
	
	public Game() {
		this.finished = false;
		this.players = new ArrayList<Player>();
	}
	
	public void loadGame() {
		
	}
	
	public boolean play(int x, int y) {
		if(!tryToAddCube(x, y)) {
			System.out.println("La posición no es válida");
			return false;
		}
		return true;
	}
	
	public String positionToString(int x, int y) {
		
	}
	
	public boolean tryToAddCube(int x, int y) {
		
	}
	
	public void flipTrapped() {
		
	}
	
	public Cube getCubeInPos() {
		
	}
	
	public int getSize() {
		
	}
	
	public boolean isFinished() {
		return this.finished;
	}
}
