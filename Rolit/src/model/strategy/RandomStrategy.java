package model.strategy;

import java.util.Random;

import model.logic.Color;
import model.replay.GameState;
import utils.Pair;

/**
 * This class represents the Random strategy for the artificial intelligences.
 * This strategy generates a random valid position for the player to play next.
 * @author PMC
 *
 */
public class RandomStrategy extends Strategy {

	public static final String NAME = "RANDOM";
	public static final String DIFFICULTY = "EASY";
	
	private Random random;
	
	/**
	 * Default constructor
	 */
	public RandomStrategy() {
		this.random = new Random();
	}
	
	@Override
	public Pair<Integer, Integer> calculateNextMove(Color currentColor, GameState state) {
		this.simplifiedBoard = new SimplifiedBoard(state, this);
		if(!simplifiedBoard.isBoardFull()) {
			Integer x, y;
			int size = simplifiedBoard.getSize();
			do {
				x = random.nextInt(size);
				y = random.nextInt(size);
			} while(!simplifiedBoard.tryToAddCube(x, y));
			return new Pair<Integer, Integer>(x, y);
		}
		else return null;
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public String getDifficulty() {
		return DIFFICULTY;
	}

	@Override
	public int simulate(Color currentColor, int depth, int alpha, int beta) {
		return 0;
	}

	
}
