package CPU;

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
}
