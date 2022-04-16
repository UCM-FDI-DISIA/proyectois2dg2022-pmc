package CPU;

import logic.Board;
import logic.Player;
import utils.Pair;

public class MinimaxStrategy extends Strategy {

	protected SimplifiedBoard simplifiedBoard;
	protected int x;
	protected int y;
	public static final int MAX_DEPTH = 3;
	private boolean maximize;
	
	public MinimaxStrategy(Player player) {
		this.player = player;
	}
	
	@Override
	public Pair<Integer, Integer> calculateNextMove(Player currentPlayer) {
		this.simulate(currentPlayer, MAX_DEPTH);
		return new Pair<Integer, Integer>(x, y);
	}
	
	public int simulate(Player currentPlayer, int depth) {
		this.maximize = (player == currentPlayer);
		Board board = game.getBoard();
		int size = board.getSize();
		int currentScore;
		int score = currentPlayer.getScore();
		for(int i = 0; i < size; i++) {
			for(int j = 0; j < size; j++) {
				if(board.tryToAddCube(i, j)) {
					currentScore = simplifiedBoard.simulateMove(i, j, currentPlayer, 0);
					if(maximize) {
						if(currentScore > score) {
							score = currentScore;
							x = i;
							y = j;
						}
					}
					else {
						if(currentScore < score) {
							score = currentScore;
							x = i;
							y = j;
						}
					}
				}	
			}
		}
		return score;
	}

}
