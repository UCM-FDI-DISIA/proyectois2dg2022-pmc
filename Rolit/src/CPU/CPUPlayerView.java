package CPU;

import control.Controller;
import logic.Color;
import utils.Pair;

public class CPUPlayerView extends PlayerView {
	private Strategy strat;

	public CPUPlayerView(Color color, Controller ctr, int dificulty) {
		super(color, ctr);
		if (dificulty == 1)
			this.strat = new RandomStrategy();
		else if (dificulty == 2)
			this.strat = new GreedyStrategy(color);
		else
			this.strat = new MinimaxStrategy(color);
	}
	
	@Override
	public void nextMove() {
		Pair<Integer, Integer> coor = this.strat.calculateNextMove(color, this.boardGUI.getState());
		this.boardGUI.clickOn(coor.getSecond(), coor.getFirst());	//Se ponen al revés porque así funcionan las CeldaGUI
	}
}
