package CPU;

import logic.Color;
import logic.Player;
import replay.State;
import utils.Pair;

public class GreedyStrategy extends MinimaxStrategy {
	
	public GreedyStrategy(Color color) {
		super(color);
	}

	@Override
	public Pair<Integer, Integer> calculateNextMove(Color currentColor, State state) {
		this.simplifiedBoard = new SimplifiedBoard(state, this);
		this.simulate(currentColor, 0);
		return new Pair<Integer, Integer>(x, y);
	}
	
}
