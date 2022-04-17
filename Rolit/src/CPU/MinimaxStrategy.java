package CPU;

import logic.Board;
import logic.Color;
import logic.Player;
import replay.State;
import utils.Pair;

public class MinimaxStrategy extends Strategy {

	protected int x;
	protected int y;
	public static final int MAX_DEPTH = 3;
	private boolean maximize;
	
	public MinimaxStrategy(Color color) {
		this.color = color;
	}
	
	@Override
	public Pair<Integer, Integer> calculateNextMove(Color currentColor, State state) {
		this.simplifiedBoard = new SimplifiedBoard(state, this);
		this.simulate(currentColor, MAX_DEPTH);
		return new Pair<Integer, Integer>(x, y);
	}
	
	public int simulate(Color currentColor, int depth) {
		this.maximize = (color.equals(currentColor));
		int size = simplifiedBoard.getSize();
		int currentScore;
		int score = simplifiedBoard.getSimulatedScore(this.color);
		for(int i = 0; i < size; i++) {
			for(int j = 0; j < size; j++) {
				if(simplifiedBoard.tryToAddCube(i, j)) {
					currentScore = simplifiedBoard.simulateMove(i, j, currentColor, 0);
					if(maximize) {
						if(currentScore > score) {
							score = currentScore;
							x = i;
							y = j;
						}
					}
					else {
						if(currentScore < score) {
							score = currentScore;
							x = i;
							y = j;
						}
					}
				}	
			}
		}
		return score;
	}

}
