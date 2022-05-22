package model.logic;

import java.util.ArrayList;
import java.util.List;
import utils.Pair;
import utils.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import model.SaveLoadManager;

/**
 * This class represents the physical Board of a Rolit game
 * @author PMC
 *
 */
public class Board implements Reportable {	
	/**
	 * Max board size
	 */
	public final static int MAX_SIZE = 15;	
	/**
	 * Cubes container
	 */
	private List<List<Cube>> matrix;
	/**
	 * Cubes container ordered by
	 */
	private List<Pair<Integer, Integer>> orderedCubeList;
	private boolean[][] shapeMatrix;
	private String shapeName;
	private int size;
	private int numValidPos;
	private int numCubes;
	private static final char SPACE = ' ';
	
	/**
	 * Constructor
	 * @param shape Board shape
	 */
	public Board(Shape shape) {
		this.numCubes = 0;
		this.matrix = new ArrayList<List<Cube>>();
		this.shapeName = shape.name();
		this.shapeMatrix = SaveLoadManager.loadShape(shape.getFilename());
		this.orderedCubeList = new ArrayList<Pair<Integer, Integer>>();
		this.size = SaveLoadManager.getShapeSize(shape.getFilename());
		this.numValidPos = 0;
		
		//we load the shape
		for (int i = 0; i < shapeMatrix.length; i++) {
			for (int j = 0; j < shapeMatrix.length; j++) {
				if(shapeMatrix[i][j]) {
					numValidPos++;
				}
			}
		}
		
		//we initialize the matrix of cubes
		for (int i = 0; i < size; i++) {
			this.matrix.add(new ArrayList<Cube>(size));
		}
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				List<Cube> column = matrix.get(i);
				column.add(null);
			}
		}
	}
	
	/**
	 * Copy constructor (deep copy)
	 * @param board Board to copy
	 */
	public Board(Board board) {		
		List<List<Cube>> m = new ArrayList<List<Cube>>();
		for (int i = 0; i < board.matrix.size(); i++) {
			List<Cube> lc = new ArrayList<Cube> ();
			for (int j = 0; j < board.matrix.size(); j++) {
				if(board.matrix.get(i).get(j) != null) {
					lc.add(new Cube(board.matrix.get(i).get(j)));
				}
				else
					lc.add(null);
			}
			m.add(lc);
		}
		
		List<Pair<Integer, Integer>> ol = new ArrayList<Pair<Integer, Integer>>();
		for (int i = 0; i < board.orderedCubeList.size(); i++) {
			Pair<Integer, Integer> p = board.orderedCubeList.get(i);
			ol.add(new Pair<Integer, Integer>(p.getFirst(), p.getSecond()));
		}
		
		this.orderedCubeList = ol;
		this.matrix = m;
		this.shapeMatrix = board.shapeMatrix;
		this.shapeName = board.shapeName;
		this.size = board.size;
		this.numCubes = board.numCubes;
	}
	
	/**
	 * This method returns the cube in the position x, y
	 * @param x x coordinate of the cube
	 * @param y y coordinate of the cube
	 * @return Cube in position (x, y)
	 */
	public Cube getCubeInPos(int x, int y) {
		return matrix.get(x).get(y);
	}
	
	/**
	 * This method checks whether if there is a cube in the position p
	 * @param p coordinates x, y of the cube
	 * @return true if there is a cube in position p or false if not
	 */
	private boolean checkIfCubeIsInList(Pair<Integer, Integer> p) {
		for (int i = 0; i < orderedCubeList.size(); ++i) {
			Pair<Integer, Integer> iPair = orderedCubeList.get(i);
			if (iPair.getFirst().equals(p.getFirst()) && iPair.getSecond().equals(p.getSecond()))
				return true;
		}
		return false;
		
	}
	
	/**
	 * This method adds the cube c to the Board, if it is in an valid position
	 * @param c Cube to add
	 * @throws IllegalArgumentException This exception is thrown if the cube is not in a valid position.
	 */
	public void addCubeInPos(Cube c) throws IllegalArgumentException {
		if (!this.tryToAddCube(c.getX(), c.getY()))
			throw new IllegalArgumentException("Non valid position for a cube");
		List<Cube> column = matrix.get(c.getX());
		column.remove(c.getY());
		column.add(c.getY(), c);
		Pair<Integer, Integer> pair = new Pair<Integer, Integer>(c.getX(), c.getY());
		if (!checkIfCubeIsInList(pair))
			orderedCubeList.add(pair);
		c.addPlayerScore();
		this.numCubes++;
	}
	
	/**
	 * This method checks if the board is full
	 * @return true if the board is full and false otherwise
	 */
	public boolean isBoardFull() {
		return numCubes == numValidPos;
	}
	
	/**
	 * This method updates the cubes that have been trapped between newCube and other cubes belonging to the newCube owner.
	 * @param newCube Added cube
	 */
	public void update(Cube newCube) {
		int posX = newCube.getX(), posY = newCube.getY();
		int newX, newY;
		int foundX = posX, foundY = posY;
		boolean found;
		Cube currentCube;

		/*
		 * We use two loops to indicate the direction in which we are searching a cube,
		 * which its color correspond to the color of the current player.
		 * 
		 * These two loops represents always eight iterations, so it doesn't imply
		 * that the complexity of the algorithm is cubic (in fact, it is linear)
		 * 
		 * */

		for (int dirX = -1; dirX <= 1; dirX++) {
			for (int dirY = -1; dirY <= 1; dirY++) {
				/*
				 * We start from the actual position (posX, posY). We move in the current
				 * direction adding to the current position (dirX, dirY).
				 */

				if (!(dirX == 0 && dirY == 0)) {
					newX = posX + dirX;
					newY = posY + dirY;
					found = false;
					boolean conected = true;
					//We check the new square and the possible cube that is in it
					while (isPositionInRange(newX, newY) && !found && conected) {
						currentCube = getCubeInPos(newX, newY);
						if (currentCube != null) {
							/*
							 * If the color of the cube corresponds to the color of the current player
							 * we stop searching, for it is this cube that we have to reach
							 * */
							if (currentCube.getColor().equals(newCube.getColor())) {
								found = true;
								foundX = newX;
								foundY = newY;
							}
							// If the cube is of another color, we keep searching
							else {
								newX += dirX;
								newY += dirY;
							}
						} else {
							conected = false; //If there is an empty square in this direction, we stop searching
						}
					}
					
					/*
					 * If the current direction given by (dirX, dirY) we have found another cube
					 * which its color corresponds to the current player, we put cubes of that color
					 * in all the squares in between
					 * 
					 * */
					if (found) {
						newX = posX + dirX;
						newY = posY + dirY;
						while (!(newX == foundX && newY == foundY)) {
							currentCube = getCubeInPos(newX, newY);

							//We change the color of the cube to the color of the actual player and
							//we update the points
							currentCube.changeOwner(newCube.getColor());
							
							newX += dirX;
							newY += dirY;
						}
					}
				}
			}
		}
		
	}
	
	/**
	 * Given a position it is converted to String
	 * @param x x coordinate
	 * @param y y coordinate
	 * @return Cube shortcut if the position is not empty or an space if it is.
	 */
	private String positionToString(int x, int y) {
		Cube cube = getCubeInPos(x, y);
		if (cube == null)
			return " ";
		else
			return cube.getColor().toString();
	}
	
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append(StringUtils.LINE_SEPARATOR);
		// Paint game
		for (int x = 0; x < size; x++) {
			for (int y = 0; y < size; y++) {
				if (!shapeMatrix[x][y]) str.append("■").append(SPACE);
				else if (shapeMatrix[x][y])str.append(positionToString(x, y)).append(SPACE);
				}
			str.append(StringUtils.LINE_SEPARATOR);
			
		}
		
		return str.toString();
	}

	/**
	 * Checks if a position is within the board boundaries
	 * @param x x coordinate
	 * @param y y coordinate
	 * @return true if the position (x,y) is in the board and false otherwise
	 */
	private boolean isPositionInRange(int x, int y) {
		return x >= 0 && x < size && y >= 0 && y < size && shapeMatrix[x][y];
	}
	
	/**
	 * This method verifies if a position is valid to place a cube
	 * @param x x coordinate
	 * @param y y coordinate
	 * @return true if the position (x, y) is valid and false otherwise.
	 */
	private boolean tryToAddCube(int x, int y) {
		if (numCubes > 0) {
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

	
	@Override
	public JSONObject report() {
		JSONObject jo = new JSONObject();
		
		JSONArray jo1 = new JSONArray();
		for (int i = 0; i < orderedCubeList.size(); i++) {
			Pair<Integer, Integer> p = orderedCubeList.get(i);
			jo1.put(getCubeInPos(p.getFirst(),p.getSecond()).report());
			
		}
		
		jo.put("shape", shapeName);
		jo.put("cubes", jo1);
		return jo;
	}
}
