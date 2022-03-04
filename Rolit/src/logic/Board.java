package logic;

import java.util.ArrayList;
import java.util.List;

public class Board {

	private final static String POS_ERROR = "Failed to add cube, its position is out of the board";

	private List<List<Cube>> matrix;
	private int size;
	private int numCubes;

	public Board(int size) {
		this.size = size;
		this.numCubes = 0;
		this.matrix = new ArrayList<List<Cube>>();
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

	public int getSize() {
		return size;
	}

	public Cube getCubeInPos(int x, int y) {
		return matrix.get(x).get(y);
	}

	public void addCubeInPos(Cube c) {
		if (c.getX() < 0 || c.getX() >= size || c.getY() < 0 || c.getY() >= size)
			throw new IllegalArgumentException(POS_ERROR);

		List<Cube> column = matrix.get(c.getX());
		column.remove(c.getY());
		column.add(c.getY(), c);
		this.numCubes++;
	}

	public int getNumCubes() {
		return numCubes;
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

							// Quitamos un punto al jugador al cual quitamos un cubo de su color
							currentCube.addScore(-1);

							// Cambiamos de color el cubo en cuestion al color del jugador actual
							currentCube.setColor(newCube.getColor());

							// Incrementamos en uno la puntuacion del jugador actual
							newCube.addScore(1);

							newX += dirX;
							newY += dirY;
						}
					}
				}
			}
		}
		
	}

	private boolean isPositionInRange(int x, int y) {
		return x >= 0 && x < size && y >= 0 && y < size;
	}

}
