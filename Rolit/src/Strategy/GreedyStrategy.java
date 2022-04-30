package Strategy;

import logic.Color;
import replay.GameState;
import utils.Pair;

public class GreedyStrategy extends MinimaxStrategy {
	
	public static final String NAME = "GREEDY";
	public static final String DIFFICULTY = "MEDIUM";
	
	public GreedyStrategy(Color color) {
		super(color);
	}

	@Override
	public Pair<Integer, Integer> calculateNextMove(Color currentColor, GameState state) {
		this.simplifiedBoard = new SimplifiedBoard(state, this);
		this.simulate(currentColor, 0);
		return new Pair<Integer, Integer>(x, y);
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
