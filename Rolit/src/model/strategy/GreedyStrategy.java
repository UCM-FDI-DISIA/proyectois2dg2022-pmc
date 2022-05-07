package model.strategy;

import model.logic.Color;
import model.replay.GameState;
import utils.Pair;

public class GreedyStrategy extends MinimaxStrategy {
	
	public static final String NAME = "GREEDY";
	public static final String DIFFICULTY = "MEDIUM";
	
	public GreedyStrategy(Color color) {
		super(color);
		this.original_depth = 0;
	}


	@Override
	public Pair<Integer, Integer> calculateNextMove(Color currentColor, GameState state) {
		this.simplifiedBoard = new SimplifiedBoard(state, this);
		this.simulate(currentColor, original_depth, -10000, 10000);
		return this.result;
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
