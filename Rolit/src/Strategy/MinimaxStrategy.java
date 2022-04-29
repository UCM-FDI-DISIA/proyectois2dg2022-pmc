package Strategy;

import java.util.Random;

import logic.Color;
import replay.GameState;
import utils.Pair;

public class MinimaxStrategy extends Strategy {

	public static final String NAME = "MINIMAX";
	public static final String DIFFICULTY = "HARD";
	
	protected int x;
	protected int y;
	public static final int MAX_DEPTH = 3;
	private boolean maximize;
	
	public MinimaxStrategy(Color color) {
		this.color = color;
	}
	
	@Override
	public Pair<Integer, Integer> calculateNextMove(Color currentColor, GameState state) {
		this.simplifiedBoard = new SimplifiedBoard(state, this);
		if(!simplifiedBoard.isBoardFull()) {
			this.simulate(currentColor, MAX_DEPTH);
			return new Pair<Integer, Integer>(x, y);
		}
		else return null;
	}
	
	public int simulate(Color currentColor, int depth) {
		this.maximize = (color.equals(currentColor));
		int size = simplifiedBoard.getSize();
		int currentScore;
		int score = simplifiedBoard.getSimulatedScore(this.color);
		if(!simplifiedBoard.isBoardEmpty()) {
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
		}
		else {
			Random random = new Random();
			do {
				x = random.nextInt(size);
				y = random.nextInt(size);
			} while(!simplifiedBoard.tryToAddCube(x, y));
			currentScore = simplifiedBoard.simulateMove(x, y, currentColor, 0);
		}
		return score;
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public String getDifficulty() {
		return DIFFICULTY;
	}

}
