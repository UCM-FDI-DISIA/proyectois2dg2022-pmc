package CPU;

import Builders.GameBuilder;
import logic.Color;
import replay.GameState;
import utils.Pair;

public abstract class Strategy {
	
	protected SimplifiedBoard simplifiedBoard;
	protected Color color;
	
	public abstract Pair<Integer, Integer> calculateNextMove(Color currentColor, GameState state);
	public abstract int simulate(Color currentColor, int depth);
	
	public static void initStrategy() {
		
	}
	
	public static Strategy[] strategies = {
		new RandomStrategy(),
		new GreedyStrategy(null),
		new MinimaxStrategy(null)
	};
	
	public static String availableStrategies() {
		StringBuilder str = new StringBuilder();
		for (Strategy s : strategies) {
			str.append(String.format("%s strategy: %s difficulty%n", s.getName(), s.getDifficulty()));
			str.append(" " + s.getName());
		}
		str.append(": ");
		return str.toString();
	}
	
	public abstract String getName();
	public abstract String getDifficulty();
}
