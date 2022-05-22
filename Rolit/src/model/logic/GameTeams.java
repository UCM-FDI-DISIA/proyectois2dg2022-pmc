package model.logic;

import java.util.List;


import org.json.JSONArray;
import org.json.JSONObject;

import model.replay.GameState;
import utils.StringUtils;
import view.RolitObserver;
/**
 * This class represents the Team game mode of Rolit.
 * @author PMC
 *
 */
public class GameTeams extends Game {
	private List<Team> teams;
	
	/**
	 * Copy constructor (deep copy)
	 * @param game Game to copy
	 */
	public GameTeams(GameTeams game) {
		super(game);
		this.teams = game.teams;
	}
	
	/**
	 * Constructor
	 * @param board Game board
	 * @param list_cubes List with the cubes that are already in the board
	 * @param list_players List with the players
	 * @param currentPlayerColor First turn player color
	 * @param teams_list List of teams
	 */
	public GameTeams(Board board, List<Cube> list_cubes, List<Player> list_players, Color currentPlayerColor, List<Team> teams_list) {
		super(board, list_cubes, list_players, currentPlayerColor);
		this.teams = teams_list;
	}
	
	@Override
	public Game copyMe() {
		return new GameTeams(this);
	}

	//The game works the same as the GameClassic part. The one who manages the new functionality of the teams
	//is the player itself when modifies its team
	@Override
	public void play() throws IllegalArgumentException {
		while(!this.pendingCubes.isEmpty()) {
			Cube c = this.pendingCubes.poll();
			
			// In case we can, we put the cube in the position and we update the board
			Cube newCube = new Cube(c.getX(), c.getY(), players.get(turnManager.getCurrentPlayerIndex()));
			this.board.addCubeInPos(newCube);
			
			this.board.update(newCube);
			// After updating the score of each player in its corresponding way,
			//we then update the team score
			for (Team team : teams)
				team.update();
			
			//We check if the game finishes in this turn
			this.finished = board.isBoardFull();
			if (this.finished) {
				this.state = new GameState("p " + newCube.getX() + " " + newCube.getY(), this);
				//We add the current state to the replay
				replay.addState(new GameState("p " + newCube.getX() + " " + newCube.getY(), copyMe()));
				
				this.onGameFinished();
			}
			//We change the turn to the next player in the list if the match has not finished
			else {
				Cube nextCube = this.turnManager.nextTurn(this.state);
				if(nextCube != null) this.addCubeToQueue(nextCube);
				onTurnPlayed();
				
				this.state = new GameState("p " + newCube.getX() + " " + newCube.getY(), this);
				//We add the current state to the replay
				replay.addState(new GameState("p " + newCube.getX() + " " + newCube.getY(),copyMe()));
				
				this.executedTurn = true;
			}	
		}
	}
	
	@Override
	public String toString() {
		StringBuilder bf = new StringBuilder();
		bf.append("Turno: ");
		bf.append(getCurrentPlayer().toString() + " from " + Team.getTeam(getCurrentPlayer())).toString();
		bf.append(StringUtils.LINE_SEPARATOR);
		bf.append(board.toString());
		return bf.toString();
	}
	
	@Override
	public JSONObject report() {
		JSONObject gameJSONObject = super.report();
		JSONArray teamsJSONArray = new JSONArray();
		for (Team team : this.teams)
			teamsJSONArray.put(team.report());
		gameJSONObject.put("teams", teamsJSONArray);
		gameJSONObject.put("type", "GameTeams");
		return gameJSONObject;		
	}
	
	@Override
	public void onTurnPlayed() {
		GameState state = new GameState(this.copyMe());
		for(RolitObserver o : observers) {
			o.onTurnPlayed(state);
		}
	}

	@Override
	protected void onGameFinished() {
		for (RolitObserver o : this.observers)
			o.onGameFinished(this.players, "Players", replay, state);		
	}

}
