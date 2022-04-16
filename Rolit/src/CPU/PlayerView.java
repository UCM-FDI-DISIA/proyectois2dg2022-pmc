package CPU;

import java.util.List;

import control.Controller;
import logic.Rival;
import replay.State;
import view.GUIView.BoardGUI;
import view.GUIView.RolitObserver;

public class PlayerView {
	protected BoardGUI board;
	private String name; 
	
	public PlayerView (String name, Controller ctr) {
		this.board = new BoardGUI(ctr);	
	}
	
	public void nextMove() {
		// no hacemos nada
	}

}
