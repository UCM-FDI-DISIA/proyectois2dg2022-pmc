package strategy;

import java.util.Random;

import logic.Board;
import logic.Game;
import logic.Player;
import utils.Pair;

public class RandomStrategy extends Strategy {

	private Random random;
	
	public RandomStrategy(Player player) {
		this.random = new Random();
		this.player = player;
	}
	
	@Override
	public Pair<Integer, Integer> calculateNextMove(Player currentPlayer) {
		Integer x, y;
		Board board = game.getBoard();
		int size = board.getSize();
		do {
			x = random.nextInt(size);
			y = random.nextInt(size);
		} while(!game.getBoard().tryToAddCube(x, y));
		return new Pair<Integer, Integer>(x, y);
	}

	
}
