package view;

import java.util.List;

import logic.GameTeams;
import logic.Team;

public class TeamsRankingTableModel extends RankingTableModel {

	public TeamsRankingTableModel(GameTeams game) {
		super(game);
		List<Team> teams = game.getTeams();
		for(Team t : teams) {
			this.rivals.add(t.getName());
		}
	}

}
