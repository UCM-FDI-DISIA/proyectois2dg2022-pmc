package CPU;

import java.util.List;

import control.Controller;
import logic.Color;
import logic.Rival;
import replay.GameState;
import view.GUIView.RolitObserver;

public class PlayerView implements RolitObserver {
	
	protected Controller ctrl;
	protected Color color;
	
	public PlayerView (Color color, Controller ctr) {
		this.ctrl = ctr;
		this.color = color;
		this.ctrl.addObserver(this);
	}
	
	public void nextMove(GameState state) {
		// no hacemos nada
	}

	@Override
	public void onFirstPlay(GameState state) {
		this.nextMove(state);
	}
	
	@Override
	public void onTurnPlayed(GameState state) {
		this.nextMove(state);		
	}

	@Override
	public void onGameFinished(List<? extends Rival> rivals, String rival) {
		// nada
	}

	@Override
	public void onRegister(GameState state) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onError(String err) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onGameStatusChange(GameState state) {
		// TODO Auto-generated method stub
		
	}
	
}
