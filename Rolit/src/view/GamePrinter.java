package view;

import logic.Game;
import utils.StringUtils;

public class GamePrinter {
	
	private static final String SPACE = " ";
	private static final int MARGIN_SIZE = 2; 
	private static final int GAP_SIZE = 3;
	private Game game;
	
	public GamePrinter(Game game) {
		this.game = game;
	}
	
	public String toString() {
		StringBuilder str = new StringBuilder();
		
		// Paint game
		for (int x = 0; x < game.getBoardSize(); x++) {
			str.append(MARGIN_SIZE);
			for (int y = 0; y < game.getBoardSize(); y++) {
				str.append(StringUtils.centre(game.positionToString(x, y), GAP_SIZE)).append(SPACE);
			}
			str.append(StringUtils.LINE_SEPARATOR);
			
		}
		
		return str.toString();
	}
}
