package CPU;

import control.Controller;
import logic.Color;
import replay.State;
import view.GUIView.BoardGUI;

public class PlayerView {
	
	protected Controller ctrl;
	protected Color color;
	
	public PlayerView (Color color, Controller ctr) {
		this.ctrl = ctr;
		this.color = color;
	}
	
	public void nextMove(State state) {
		// no hacemos nada
	}
	
}
