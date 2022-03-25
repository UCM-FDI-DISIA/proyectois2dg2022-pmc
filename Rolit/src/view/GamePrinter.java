package view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import logic.GameClassic;
import logic.Player;
import utils.StringUtils;

public class GamePrinter {
	
	private static final String SPACE = " ";
	private static final String RANKING = "RANKING DEL ROLIT";
	private static final String MSG_POS = "En la posicion numero ";
	private static final String SEPARATOR = ": ";
	private static final String MSG_REY = "QUIEN SERA EL REYYYYYY?????? :)";
	private static final String MSG_GOOD_LUCK = "Suerte para la siguiente :)";
	private GameClassic game;
	
	public GamePrinter(GameClassic game) {
		this.game = game;
	}
	
	public String toString() {
		return game.toString();
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

}
