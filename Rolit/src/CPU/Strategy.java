package CPU;

import logic.Color;
import logic.Game;
import logic.Player;
import replay.State;
import utils.Pair;

public abstract class Strategy {
	
	protected SimplifiedBoard simplifiedBoard;
	protected Color color;
	
	public abstract Pair<Integer, Integer> calculateNextMove(Color currentColor, State state);
	public abstract int simulate(Color currentColor, int depth);
	
	public static void initStrategy() {
		
	}
}
