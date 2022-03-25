package logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameTeams extends GameClassic {
	private List<Team> teams;
		
	public GameTeams(GameTeams game) {
		super(game);
		// FIXME esto puede que este mal y no haga bien la copia
		Collections.copy(game.teams, this.teams);
	}
	
	public GameTeams(Board board, List<Cube> list_cubes, List<Player> list_players, Color currentPlayerColor, List<Team> teams_list) {
		super(board, list_cubes, list_players, currentPlayerColor);
		this.teams = teams_list;
	}
}
