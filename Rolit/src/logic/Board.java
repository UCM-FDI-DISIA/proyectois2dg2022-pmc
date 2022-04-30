package logic;

import java.util.ArrayList;
import java.util.List;

import control.SaveLoadManager;
import utils.Pair;
import utils.StringUtils;

import org.json.JSONArray;
import org.json.JSONObject;
public class Board implements Reportable {	
	
	public final static int MAX_SIZE = 15;	
	private List<List<Cube>> matrix;
	private static List<Pair<Integer, Integer>> orderedCubeList = new ArrayList<Pair<Integer, Integer>>(); //FIXME SI FALLA ALGO DE LA RED ES PORQUE HAY QUE PONER ESTO ESTATICO
	private boolean[][] shapeMatrix;
	private String shapeName;
	private int size;
	private int numCubes;
	private static final char SPACE = ' ';
	
	public Board(Shape shape) {
		this.numCubes = 0;
		this.matrix = new ArrayList<List<Cube>>();
		this.shapeName = shape.name();
		this.shapeMatrix = SaveLoadManager.loadShape(shape.getFilename());
		this.size = SaveLoadManager.getShapeSize(shape.getFilename());
		//aqui se llamara a una funcion que cargue la mtriz de booleanos
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
		
		this.matrix = m;
		this.shapeMatrix = board.shapeMatrix;
		this.shapeName = board.shapeName;
		this.size = board.size;
		this.numCubes = board.numCubes;
	}
	
	//por ahora es para los tests
	public void clearOrderedCubeList() {
		orderedCubeList = new ArrayList<Pair<Integer, Integer>>(); 
	}
	
	public Cube getCubeInPos(int x, int y) {
		return matrix.get(x).get(y);
	}
	
	private boolean checkIfCubeIsInList(Pair<Integer, Integer> p) {
		for (int i = 0; i < orderedCubeList.size(); ++i) {
			Pair<Integer, Integer> iPair = orderedCubeList.get(i);
			if (iPair.getFirst().equals(p.getFirst()) && iPair.getSecond().equals(p.getSecond()))
				return true;
		}
		return false;
		
	}
	
	public void addCubeInPos(Cube c) throws IllegalArgumentException {
		if (!this.tryToAddCube(c.getX(), c.getY()))
			throw new IllegalArgumentException("non valid position for a cube");
		List<Cube> column = matrix.get(c.getX());
		column.remove(c.getY());
		column.add(c.getY(), c);
		Pair<Integer, Integer> pair = new Pair<Integer, Integer>(c.getX(), c.getY());
		if (!checkIfCubeIsInList(pair))
			orderedCubeList.add(pair);
		c.addPlayerScore();
		this.numCubes++;
	}
	
	public boolean isBoardFull() {
		return numCubes == size * size;
	}
	
	public void update(Cube newCube) {
		int posX = newCube.getX(), posY = newCube.getY();
		int newX, newY;
		int foundX = posX, foundY = posY; // Las inicializo porque si no Eclipse se queja
		boolean found;
		Cube currentCube;

		// Hacemos dos bucles para indicar la direccion en la que vamos a buscar un cubo
		// del color del jugador actual
		// Estos dos bucles representan siempre 8 iteraciones, por lo que no implican
		// que el coste del algoritmo sea cubico (es lineal)
		for (int dirX = -1; dirX <= 1; dirX++) {
			for (int dirY = -1; dirY <= 1; dirY++) {
				// Partiendo de la posicion actual (posX, posY), nos movemos en la direccion
				// actual
				// sumando a la posicion actual (dirX, dirY)
				if (!(dirX == 0 && dirY == 0)) {
					newX = posX + dirX;
					newY = posY + dirY;
					found = false;
					boolean conected = true;
					// Comprobamos la nueva casilla y el posible cubo que haya en ella
					while (isPositionInRange(newX, newY) && !found && conected) {
						currentCube = getCubeInPos(newX, newY);
						if (currentCube != null) {
							// Si el cubo es del color del jugador actual dejamos de buscar, es hasta este
							// hasta el que tenemos que llegar
							if (currentCube.getColor().equals(newCube.getColor())) {
								found = true;
								foundX = newX;
								foundY = newY;
							}
							// Si el cubo es de otro color seguimos buscando
							else {
								newX += dirX;
								newY += dirY;
							}
						} else {
							conected = false; // Si hay alguna casilla vacia en esta direccion dejamos de buscar
						}
					}
					// Si en la direccion dada por (dirX, dirY) hemos encontrado otro cubo del color
					// del jugador actual
					// ponemos cubos del color en cuestion en todas las casillas entre medias
					if (found) {
						newX = posX + dirX;
						newY = posY + dirY;
						while (!(newX == foundX && newY == foundY)) {
							currentCube = getCubeInPos(newX, newY);

							// Cambiamos de color el cubo en cuestion al color del jugador actual y actualizamos los puntos
							currentCube.changeOwner(newCube.getColor());
							
							newX += dirX;
							newY += dirY;
						}
					}
				}
			}
		}
		
	}
	
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

	private boolean isPositionInRange(int x, int y) {
		return x >= 0 && x < size && y >= 0 && y < size && shapeMatrix[x][y];
	}
	
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
