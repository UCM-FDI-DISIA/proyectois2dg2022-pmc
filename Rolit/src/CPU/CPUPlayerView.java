package CPU;

import commands.PlaceCubeCommand;
import control.Controller;
import logic.Color;
import replay.State;
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
	public void nextMove(State state) {
		if (this.color.equals(Color.valueOfIgnoreCase(state.report().getJSONObject("game").getString("turn").charAt(0)))) {
			Pair<Integer, Integer> coor = this.strat.calculateNextMove(color, state);
			try {
				this.ctrl.executeCommand(new PlaceCubeCommand(coor.getFirst(), coor.getSecond()));
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}		
	}
}
