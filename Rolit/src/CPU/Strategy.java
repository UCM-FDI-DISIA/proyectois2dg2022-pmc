package CPU;

import logic.Game;
import logic.Player;
import utils.Pair;

public abstract class Strategy {
	
	protected static Game game;
	protected Player player;
	
	public abstract Pair<Integer, Integer> calculateNextMove(Player currentPlayer);
	
	public static void initStrategy(Game game) {
		Strategy.game = game;
	}
}
