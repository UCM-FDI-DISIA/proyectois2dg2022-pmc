package Strategy;

import logic.Color;
import replay.GameState;
import utils.Pair;

public abstract class Strategy {
	
	protected SimplifiedBoard simplifiedBoard;
	protected Color color;
	
	public abstract Pair<Integer, Integer> calculateNextMove(Color currentColor, GameState state);
	public abstract int simulate(Color currentColor, int depth, int alpha, int beta);
	
	public static Strategy[] strategies = {
		new RandomStrategy(),
		new GreedyStrategy(null),
		new MinimaxStrategy(null)
	};
	
	public static String availableStrategies() {
		StringBuilder str = new StringBuilder();
		for (Strategy s : strategies) {
			str.append(String.format("%s strategy: %s difficulty%n", s.getName(), s.getDifficulty()));
		}
		return str.toString();
	}
	
	public static Strategy parse(Color color, String type) {
		Strategy s = null;
		switch(type) {
		case "RANDOM":
			s = new RandomStrategy();
			break;
		case "GREEDY":
			s = new GreedyStrategy(color);
			break;
		case "MINIMAX":
			s = new MinimaxStrategy(color);
			break;
		default:
			throw new IllegalArgumentException("Not a parseable difficulty");
		}
		return s;
	}
	
	public abstract String getName();
	public abstract String getDifficulty();
	
	@Override
	public String toString() {
		return getName(); 
	}
}
