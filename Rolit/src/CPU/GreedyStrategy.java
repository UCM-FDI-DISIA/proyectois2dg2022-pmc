package CPU;

import logic.Player;
import utils.Pair;

public class GreedyStrategy extends MinimaxStrategy {
	
	public GreedyStrategy(Player player) {
		super(player);
	}

	@Override
	public Pair<Integer, Integer> calculateNextMove(Player currentPlayer) {
		this.simulate(player, 0);
		return new Pair<Integer, Integer>(x, y);
	}
	
}
