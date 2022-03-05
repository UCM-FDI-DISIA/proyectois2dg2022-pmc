package view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import logic.Game;
import logic.Player;
import utils.StringUtils;

public class GamePrinter {
	
	private static final String SPACE = " ";
	private static final String RANKING = "RANKING DEL ROLIT";
	private static final String MSG_POS = "En la posicion numero ";
	private static final String SEPARATOR = ": ";
	private static final String MSG_REY = "QUIEN SERA EL REYYYYYY?????? :)";
	private static final String MSG_GOOD_LUCK = "Suerte para la siguiente :)";
	private Game game;
	
	public GamePrinter(Game game) {
		this.game = game;
	}
	
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append(StringUtils.LINE_SEPARATOR);
		// Paint game
		str.append(StringUtils.repeat('-', game.getBoardSize() * 2)).append(StringUtils.LINE_SEPARATOR);	//TODO El margen tiene que depender del tamaño
		for (int x = 0; x < game.getBoardSize(); x++) {
			for (int y = 0; y < game.getBoardSize(); y++) {
				str.append(game.positionToString(x, y)).append(SPACE);
			}
			str.append(StringUtils.LINE_SEPARATOR);
			
		}
		str.append(StringUtils.repeat('-', game.getBoardSize() * 2)).append(StringUtils.LINE_SEPARATOR);
		return str.toString();
	}
	
	public String showRanking() {
		List<Player> players = new ArrayList<Player>(game.getPlayers());
		Collections.sort(players);
		StringBuilder str = new StringBuilder(RANKING);
		
		str.append(StringUtils.LINE_SEPARATOR).append(MSG_REY).append(StringUtils.LINE_SEPARATOR);
		
		for (int i = 0; i < players.size(); ++i) {
			str.append(MSG_POS).append(i + 1).append(SEPARATOR).append(players.get(i).getName()).append(StringUtils.LINE_SEPARATOR);
		}
		
		str.append(MSG_GOOD_LUCK).append(StringUtils.LINE_SEPARATOR);
		
		return str.toString(); 
	}
	
	public String showTurn() {
		return String.format("Turno de %s (%s)", game.getCurrentPlayer().getName(), game.getCurrentColor().toString());
	}
}
