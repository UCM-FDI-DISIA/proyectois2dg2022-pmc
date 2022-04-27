package view.GUIView;


import logic.Game;

public class GamePrinter {
	
	private Game game;
	
	public GamePrinter(Game game) {
		this.game = game;
	}
	
	public String toString() {
		return game.toString();
	}
	
	

}
