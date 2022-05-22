package model.strategy;

import model.logic.Color;
import model.replay.GameState;
import utils.Pair;

/**
 * This class represents the Greedy strategy for the artificial intelligences.
 * This strategy searches for the immediate next move that will grant the player the most points.
 * @author PMC
 *
 */
public class GreedyStrategy extends MinimaxStrategy {
	
	public static final String NAME = "GREEDY";
	public static final String DIFFICULTY = "MEDIUM";
	
	/**
	 * Default constructor
	 * @param color Color of the owner player
	 */
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
