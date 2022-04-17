package CPU;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.json.JSONArray;
import org.json.JSONObject;

import control.SaveLoadManager;
import logic.Board;
import logic.Color;
import logic.Cube;
import logic.Game;
import logic.Player;
import logic.Shape;
import replay.State;
import utils.Pair;

public class SimplifiedBoard {

	private Color[][] matrix;
	private boolean[][] availablePositions;
	private List<Integer> scores;
	private Stack<Pair<Pair<Integer, Integer>, Color>> changesStack;	//TODO Podría hacerse una clase Coordenadas para los Pair<Integer, Integer>
	private Stack<Integer> numberOfChangesStack;
	private State state;
	private Strategy strat;
	private int numCubes;
	
	public SimplifiedBoard(State state, Strategy strat) {
		this.numCubes = 0;
		JSONObject json = state.report();
		JSONObject jGame = json.getJSONObject("game");
		JSONObject jBoard = jGame.getJSONObject("board");
		Shape shape = Shape.valueOfIgnoreCase(jBoard.getString("shape"));
		this.availablePositions = SaveLoadManager.loadShape(shape);
		int length = this.availablePositions.length;
		JSONArray cubes = jBoard.getJSONArray("cubes");
		this.scores = new ArrayList<>();
		for(int i = 0; i < Color.size(); i++) {
			this.scores.add(0);
		}
		this.matrix = new Color[length][length];
		for(int i = 0; i < cubes.length(); i++) {
			JSONArray ja = cubes.getJSONObject(i).getJSONArray("pos");
			Color auxColor = Color.valueOfIgnoreCase(cubes.getJSONObject(i).getString("color").charAt(0));
			this.matrix[ja.getInt(0)][ja.getInt(1)] = auxColor;
			this.scores.set(auxColor.ordinal(), this.scores.get(auxColor.ordinal()) + 1);
			this.numCubes++;
		}
		
		this.changesStack = new Stack<>();
		this.numberOfChangesStack = new Stack<>();
		this.strat = strat;
		this.state = state;
	}
	
	public int simulateMove(int x, int y, Color color, int maxDepth) {
		this.applyChanges(x, y, color);
		int currentScore = this.scores.get(color.ordinal());
		if(maxDepth > 0) {
			JSONArray players = state.report().getJSONObject("game").getJSONArray("players");
			boolean found = false;
			int currentIndex = 0;
			int i = 0;
			while(!found) {
				Color auxColor = Color.valueOfIgnoreCase(players.getJSONObject(i).getString("color").charAt(0));
				if(auxColor.equals(color)) {
					found = true;
					currentIndex = i;
				}
				i++;
			}
			int nextIndex = (currentIndex + 1) % players.length();
			Color nextColor = Color.valueOfIgnoreCase(players.getJSONObject(nextIndex).getString("color").charAt(0));
			currentScore = strat.simulate(nextColor, maxDepth - 1);
		}
		this.revertChanges(color);
		return currentScore;
	}
	
	private void applyChanges(int x, int y, Color currentColor) {
		//El funcionamiento de este método es muy similar al método update() de Board, adaptado a las necesidades técnicas de esta clase
		int posX = x, posY = y;
		int newX, newY;
		int foundX = posX, foundY = posY; // Las inicializo porque si no Eclipse se queja
		boolean found;
		Color color;
		int numberOfChanges = 0;

		this.matrix[posX][posY] = currentColor;
		
		for (int dirX = -1; dirX <= 1; dirX++) {
			for (int dirY = -1; dirY <= 1; dirY++) {
				if (!(dirX == 0 && dirY == 0)) {
					newX = posX + dirX;
					newY = posY + dirY;
					found = false;
					boolean conected = true;
					while (isPositionInRange(newX, newY) && !found && conected) {
						color = this.matrix[newX][newY];
						if (color != null) {
							if (color.equals(currentColor)) {
								found = true;
								foundX = newX;
								foundY = newY;
							}
							else {
								newX += dirX;
								newY += dirY;
							}
						} else {
							conected = false;
						}
					}
					if (found) {
						newX = posX + dirX;
						newY = posY + dirY;
						while (!(newX == foundX && newY == foundY)) {
							color = this.matrix[newX][newY];
							Pair<Integer, Integer> coords = new Pair<Integer, Integer>(newX, newY);
							this.changesStack.add(new Pair<Pair<Integer, Integer>, Color>(coords, color));
							this.scores.set(color.ordinal(), this.scores.get(color.ordinal()) - 1);
							this.scores.set(currentColor.ordinal(), this.scores.get(currentColor.ordinal()) + 1);
							this.matrix[newX][newY] = color;
							numberOfChanges++;
							newX += dirX;
							newY += dirY;
						}
					}
				}
			}
		}
		this.numberOfChangesStack.push(numberOfChanges);
		this.numCubes++;
	}
	
	private void revertChanges(Color currentColor) {
		for(int i = 0; i < this.numberOfChangesStack.peek(); i++) {
			Pair<Integer, Integer> coords = this.changesStack.get(0).getFirst();
			Color color = this.changesStack.get(0).getSecond();
			this.scores.set(color.ordinal(), this.scores.get(color.ordinal()) + 1);
			this.scores.set(currentColor.ordinal(), this.scores.get(currentColor.ordinal()) - 1);
			this.matrix[coords.getFirst()][coords.getSecond()] = color;
			this.changesStack.pop();
		}
		this.numberOfChangesStack.pop();
		this.numCubes--;
	}

	private boolean isPositionInRange(int x, int y) {
		return x >= 0 && x < this.matrix.length && y >= 0 && y < this.matrix.length;
	}
	
	public int getSize() {
		return this.matrix.length;
	}
	
	public int getSimulatedScore(Color color) {
		return this.scores.get(color.ordinal());
	}
	
	public boolean tryToAddCube(int x, int y) {
		if (this.numCubes > 0) {
			boolean nearbyCube = false;
			if (!isPositionInRange(x, y) || this.matrix[x][y] != null || !this.availablePositions[x][y])
				return false;
			for (int i = -1; i <= 1; i++) {
				for (int j = -1; j <= 1; j++) {
					if (isPositionInRange(x + i, y + j) && matrix[x + i][y + j] != null && this.availablePositions[x + i][y + j]) {
						nearbyCube = true;
					}
				}
			}
			return nearbyCube;
		} else
			return isPositionInRange(x, y);
	}
	
}
