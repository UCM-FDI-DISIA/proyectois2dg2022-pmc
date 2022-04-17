package CPU;

import java.util.Random;

import logic.Color;
import replay.State;
import utils.Pair;

public class RandomStrategy extends Strategy {

	private Random random;
	
	public RandomStrategy() {
		this.random = new Random();
	}
	
	@Override
	public Pair<Integer, Integer> calculateNextMove(Color currentColor, State state) {
		this.simplifiedBoard = new SimplifiedBoard(state, this);
		Integer x, y;
		int size = simplifiedBoard.getSize();
		do {
			x = random.nextInt(size);
			y = random.nextInt(size);
		} while(!simplifiedBoard.tryToAddCube(x, y));
		return new Pair<Integer, Integer>(x, y);
	}

	@Override
	public int simulate(Color currentColor, int depth) {
		return 0;
	}

	
}
