package model.strategy;

import java.util.Random;

import model.logic.Color;
import model.replay.GameState;
import utils.Pair;

/**
 * This class represents the Minimax strategy for the artificial intelligences.
 * This strategy calculates the next move for the player aplying the concept of the Minimax algorithm.
 * @author PMC
 *
 */
public class MinimaxStrategy extends Strategy {

	public static final String NAME = "MINIMAX";
	public static final String DIFFICULTY = "HARD";
	
	protected Pair<Integer, Integer> result;
	public static final int MAX_DEPTH = 2;
	protected int original_depth;
	private boolean maximize;
	
	/**
	 * Default constructor
	 * @param color Color of the owner player
	 */
	public MinimaxStrategy(Color color) {
		this.color = color;
		this.original_depth = MAX_DEPTH;
	}
	
	@Override
	public Pair<Integer, Integer> calculateNextMove(Color currentColor, GameState state) {
		this.result = null;
		this.simplifiedBoard = new SimplifiedBoard(state, this);
		if(!simplifiedBoard.isBoardFull()) {
			this.simulate(currentColor, MAX_DEPTH, -10000, 10000);
			return result;
		}
		else return null;
	}
	
	@Override
	public int simulate(Color currentColor, int depth, int alpha, int beta) {
		this.maximize = (color.equals(currentColor));
		int size = simplifiedBoard.getSize();
		int currentScore;
		int score = simplifiedBoard.getSimulatedScore(this.color);
		boolean keepSearching = true;
		if(!simplifiedBoard.isBoardEmpty()) {
			for(int i = 0; i < size && keepSearching; i++) {
				for(int j = 0; j < size && keepSearching; j++) {
					if(simplifiedBoard.tryToAddCube(i, j)) {
						currentScore = simplifiedBoard.simulateMove(i, j, currentColor, depth, alpha, beta);
						if(maximize) {
							if(currentScore >= score) {
								score = currentScore;
								if(depth == original_depth) {
									this.result = new Pair<Integer, Integer>(i, j);
								}
							}
							if(currentScore >= beta)
								keepSearching = false;
							if(currentScore > alpha) alpha = currentScore;
						}
						else {
							if(currentScore <= score) {
								score = currentScore;
								if(depth == original_depth) {
									this.result = new Pair<Integer, Integer>(i, j);
								}
							}
							if(currentScore <= alpha)
								keepSearching = false;
							if(currentScore < beta) beta = currentScore;
						}
					}	
				}
			}
			if(this.result == null) {
				Random random = new Random();
				int x, y;
				do {
					x = random.nextInt(size);
					y = random.nextInt(size);
				} while(!simplifiedBoard.tryToAddCube(x, y));
				score = simplifiedBoard.simulateMove(x, y, currentColor, 0, -10000, 10000);
				if(depth == original_depth) this.result = new Pair<Integer, Integer>(x, y);
			}
		}
		else {
			Random random = new Random();
			int x, y;
			do {
				x = random.nextInt(size);
				y = random.nextInt(size);
			} while(!simplifiedBoard.tryToAddCube(x, y));
			score = simplifiedBoard.simulateMove(x, y, currentColor, 0, -10000, 10000);
			if(depth == original_depth) this.result = new Pair<Integer, Integer>(x, y);
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
