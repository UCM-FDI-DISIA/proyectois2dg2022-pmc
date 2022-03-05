package logic;

import java.util.List;

public interface Saveable {
	public List<Player> getPlayers();
	
	public Color getCurrentColor();
	
	public List<Cube> saveBoard();
	
	public void loadGame(List<Cube> cubes, List<Player> players, Color currentPlayerColor, int boardSize);
}
