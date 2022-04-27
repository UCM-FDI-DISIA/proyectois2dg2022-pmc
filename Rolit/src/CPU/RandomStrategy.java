package CPU;

import java.util.Random;

import logic.Color;
import replay.GameState;
import utils.Pair;

public class RandomStrategy extends Strategy {

	public static final String NAME = "RANDOM";
	public static final String DIFFICULTY = "EASY";
	
	private Random random;
	
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
	public int simulate(Color currentColor, int depth) {
		return 0;
	}

	@Override
	protected String getName() {
		return NAME;
	}

	@Override
	protected String getDifficulty() {
		return DIFFICULTY;
	}

	
}
