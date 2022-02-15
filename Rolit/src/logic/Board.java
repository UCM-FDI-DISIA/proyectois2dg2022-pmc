package logic;

import java.util.ArrayList;
import java.util.List;

public class Board {
	//getSize()
	//getCubeInPos();
	
	private List<List<Cube>> matrix;
	
	public Board(int size) {
		this.matrix = new ArrayList<List<Cube>>();
		for(int i = 0; i < size; i++) {
			this.matrix.add(new ArrayList<Cube> (size));
		}
	}
}
