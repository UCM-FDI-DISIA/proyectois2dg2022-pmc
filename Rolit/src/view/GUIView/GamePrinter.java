package view.GUIView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import logic.Game;
import logic.Player;
import utils.StringUtils;

public class GamePrinter {
	
	private Game game;
	
	public GamePrinter(Game game) {
		this.game = game;
	}
	
	public String toString() {
		return game.toString();
	}
	
	

}
