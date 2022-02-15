package logic;

import java.util.ArrayList;
import java.util.List;

public class Board {
	
	private final static String POS_ERROR = "Failed to add cube, its position is out of the board";
	
	private List<List<Cube>> matrix;
	private int size;
	
	public Board(int size) {
		this.size = size;
		this.matrix = new ArrayList<List<Cube>>();
		for(int i = 0; i < size; i++) {
			this.matrix.add(new ArrayList<Cube> (size));
		}
	}
	
	public int getSize() {
		return size;
	}
	
	public Cube getCubeInPos(int x, int y) {
		return matrix.get(x).get(y);
	}
	
	public void addCubeInPos(Cube c) {
		if(c.getX() < 0 || c.getX() >= size || c.getY() < 0 || c.getY() >= size)
			throw new IllegalArgumentException(POS_ERROR);
		
		List<Cube> column = matrix.get(c.getX());
		column.add(c.getY(), c);
	}
	
}
