package CPU;

import java.util.List;

import control.Controller;
import logic.Rival;
import replay.State;
import utils.Pair;

public class CPUPlayer extends PlayerView {
	private Strategy strat;

	public CPUPlayer(String name, Controller ctr, int dificulty) {
		super(name, ctr);
		if (dificulty == 0)
			this.strat = new RandomStrategy();
		else if (dificulty == 1)
			this.strat = new GreedyStrategy();
		else
			this.strat = new MinimaxStrategy();
	}
	
	@Override
	public void nextMove() {
		Pair<Integer, Integer> coor = this.strat.calculateNextMove(player, this.board);
		this.board.clickOn(coor.getFirst(), coor.getSecond());
	}
}
