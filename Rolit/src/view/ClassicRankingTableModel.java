package view;

import java.util.List;

import logic.Game;
import logic.GameClassic;
import logic.Player;

public class ClassicRankingTableModel extends RankingTableModel {
	
	public ClassicRankingTableModel(GameClassic game) {
		super(game);
		List<Player> players = game.getPlayers();
		for(Player p : players) {
			this.rivals.add(p.getName());
		}
		this._colNames[0] = "Players";
	}

}
