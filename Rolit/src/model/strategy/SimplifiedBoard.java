package model.strategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.json.JSONArray;
import org.json.JSONObject;

import model.SaveLoadManager;
import model.logic.Color;
import model.logic.Shape;
import model.replay.GameState;
import utils.Pair;

/**
 * This class is created as a purely functional representation of the board,
 * made to make simulations on it easier and quicker.
 * @author PMC
 *
 */
public class SimplifiedBoard {
	
	/**
	 * Color matrix that represents the cubes that have already been placed in the real board
	 */
	private Color[][] matrix;
	
	/**
	 * Boolean matrix that represents the shape of the board, defining which positions are in range and which positions are not
	 */
	private boolean[][] availablePositions;
	
	/**
	 * List that contains the scores of every player in the current state of the simulation
	 */
	private List<Integer> scores;
	
	/**
	 * Stack containing the changes that have been made to the board throughout the simulation
	 * These will eventually be reverted to bring the board back to its original state
	 */
	private Stack<Pair<Pair<Integer, Integer>, Color>> changesStack;
	
	/**
	 * Stack containing the number of changes that have been made to the board during the current step of the simulation
	 * Used to know the amount of changes that have to be reverted once the current simulation step is over
	 */
	private Stack<Integer> numberOfChangesStack;
	
	/**
	 * Current state of the real game, before any new change is made
	 */
	private GameState state;

	/**
	 * Strategy that owns the SimplifiedBoard instant and starts the simulation
	 */
	private Strategy strat;
	
	/**
	 * Number of cubes in the current simulation step
	 */
	private int numCubes;
	
	/**
	 * Maximum number of cubes that can be placed in the board until it is full
	 */
	private int maxCapacity;
	
	/**
	 * Constructor
	 * @param state The current state of the game
	 * @param strat The Strategy that owns the SimplifiedBoard instance and starts the simulation
	 */
	public SimplifiedBoard(GameState state, Strategy strat) {
		this.numCubes = 0;
		JSONObject json = state.report();
		JSONObject jGame = json.getJSONObject("game");
		JSONObject jBoard = jGame.getJSONObject("board");
		Shape shape = Shape.valueOfIgnoreCase(jBoard.getString("shape"));
		this.availablePositions = SaveLoadManager.loadShape(shape);
		int length = this.availablePositions.length;
		this.maxCapacity = 0;
		for(int i = 0; i < length; i++) {
			for(int j = 0; j < length; j++) {
				if(availablePositions[i][j]) this.maxCapacity++;
			}
		}
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
	
	/**
	 * Method that simulated the placing of a cube in the (x,y) coordinates
	 * @param x First component of the coordinates
	 * @param y Second component of the coordinates
	 * @param color Color of the player whose move is being simulated
	 * @param maxDepth Number of turns left to explore after the current move is simulated
	 * @param alpha Alpha value for the alpha-beta prune method
	 * @param beta Beta value for the alpha-beta prune method
	 * @return The score that will be guaranteed to the player after playing the calculated move
	 */
	public int simulateMove(int x, int y, Color color, int maxDepth, int alpha, int beta) {
		this.applyChanges(x, y, color);
		int currentScore = this.scores.get(color.ordinal());
		if(maxDepth > 0 && !this.isBoardFull()) {
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
			currentScore = strat.simulate(nextColor, maxDepth - 1, alpha, beta);
		}
		this.revertChanges(x, y, color);
		return currentScore;
	}
	
	/**
	 * Method used to update the board to its new simulated state
	 * @param x First component of the coordinates
	 * @param y Second component of the coordinates
	 * @param currentColor Color of the player whose move is being simulated
	 */
	private void applyChanges(int x, int y, Color currentColor) {
		//This method is very similar to the update() method of Board, adapting it to the technical necessities of this class
		int posX = x, posY = y;
		int newX, newY;
		int foundX = posX, foundY = posY;
		boolean found;
		Color color;
		int numberOfChanges = 0;

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
							this.matrix[newX][newY] = currentColor;
							numberOfChanges++;
							newX += dirX;
							newY += dirY;
						}
					}
				}
			}
		}
		
		//Nuevo cubo
		this.matrix[posX][posY] = currentColor;
		this.scores.set(currentColor.ordinal(), this.scores.get(currentColor.ordinal()) + 1);
		this.numCubes++;
		
		this.numberOfChangesStack.push(numberOfChanges);
	}
	
	/**
	 * Method used to revert the changes made to the board in the current step of the simulation and to bring it back to its previous state
	 * @param x First component of the coordinates
	 * @param y Second component of the coordinates
	 * @param color Color of the player whose move is being simulated
	 */
	private void revertChanges(int x, int y, Color currentColor) {
		for(int i = 0; i < this.numberOfChangesStack.peek(); i++) {
			Pair<Integer, Integer> coords = this.changesStack.peek().getFirst();
			Color color = this.changesStack.peek().getSecond();
			this.changesStack.pop();
			this.scores.set(color.ordinal(), this.scores.get(color.ordinal()) + 1);
			this.scores.set(currentColor.ordinal(), this.scores.get(currentColor.ordinal()) - 1);
			this.matrix[coords.getFirst()][coords.getSecond()] = color;
		}
		this.matrix[x][y] = null;
		this.scores.set(currentColor.ordinal(), this.scores.get(currentColor.ordinal()) - 1);
		this.numCubes--;
		this.numberOfChangesStack.pop();
	}

	/**
	 * Method used to check if a position is within the range of the board
	 * @param x First component of the coordinates
	 * @param y Second component of the coordinates
	 * @return True if it is withing range, false otherwise
	 */
	private boolean isPositionInRange(int x, int y) {
		return x >= 0 && x < this.matrix.length && y >= 0 && y < this.matrix.length;
	}
	
	/**
	 * Getter of the size of the board's side
	 * @return Size of the board's side
	 */
	public int getSize() {
		return this.matrix.length;
	}
	
	/**
	 * Getter of the score of the player who matches the color in the current state of the simulation
	 * @param color Color of the player whose score wants to be known
	 * @return Score of the player who matches the color in the current state of the simulation
	 */
	public int getSimulatedScore(Color color) {
		return this.scores.get(color.ordinal());
	}
	
	/**
	 * Method that tries to add a cube to the board at the given coordinates
	 * @param x First component of the coordinates
	 * @param y Second component of the coordinates
	 * @return True if the cube can be added to the board, false otherwise
	 */
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
			return isPositionInRange(x, y) && this.availablePositions[x][y];
	}
	
	/**
	 * Method that returns a boolean indicating if the board is empty
	 * @return True if the board is empty, false otherwise
	 */
	public boolean isBoardEmpty() {
		return this.numCubes == 0;
	}
	
	/**
	 * Method that returns a boolean indicating if the board is full
	 * @return True if the board is full, false otherwise
	 */
	public boolean isBoardFull() {
		return this.numCubes == this.maxCapacity;
	}
	
}
