package CPU;

import control.Controller;
import logic.Color;
import view.GUIView.BoardGUI;

public class PlayerView {
	
	protected BoardGUI boardGUI;
	protected Color color;
	
	public PlayerView (Color color, Controller ctr) {
		this.boardGUI = new BoardGUI(ctr);
		this.color = color;
	}
	
	public void nextMove() {
		// no hacemos nada
	}

	public BoardGUI getBoardGUI() {
		return this.boardGUI;
	}
	
}
