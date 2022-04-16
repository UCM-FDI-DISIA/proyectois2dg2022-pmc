package CPU;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import logic.Board;
import logic.Color;
import logic.Cube;
import logic.Game;
import logic.Player;
import utils.Pair;

public class SimplifiedBoard {

	private Color[][] matrix;
	private List<Integer> scores;
	private Stack<Pair<Pair<Integer, Integer>, Color>> changesStack;	//TODO Podría hacerse una clase Coordenadas para los Pair<Integer, Integer>
	private Stack<Integer> numberOfChangesStack;
	private MinimaxStrategy strat;
	private Game game;
	
	public SimplifiedBoard(Game game, Board board, MinimaxStrategy strat) {
		this.matrix = new Color[board.getSize()][board.getSize()];
		for(int i = 0; i < board.getSize(); i++) {
			for(int j = 0; j < board.getSize(); j++) {
				Cube cube = board.getCubeInPos(i, j);
				if(cube != null)
					this.matrix[i][j] = cube.getColor();
				else
					this.matrix[i][j] = null;
			}
		}
		this.scores = new ArrayList<>();
		for(int i = 0; i < Color.size(); i++) {
			this.scores.add(0);
		}
		this.changesStack = new Stack<>();
		this.strat = strat;
	}
	
	public int simulateMove(int x, int y, Player player, int maxDepth) {
		this.applyChanges(x, y, player.getColor());
		int currentScore = this.scores.get(player.getColor().ordinal());
		if(maxDepth > 0) {
			currentScore = strat.simulate(game.getNextPlayer(player), maxDepth - 1);
		}
		this.revertChanges(player.getColor());
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
	}

	private boolean isPositionInRange(int x, int y) {
		return x >= 0 && x < this.matrix.length && y >= 0 && y < this.matrix.length;
	}
	
}
