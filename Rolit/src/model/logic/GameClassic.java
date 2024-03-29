package model.logic;

import java.util.List;
import org.json.JSONObject;

import model.replay.GameState;
import utils.StringUtils;
import view.RolitObserver;

/**
 * This class represents the Classic game mode of Rolit.
 * @author PMC
 *
 */
public class GameClassic extends Game {
	
	/**
	 * Copy constructor (deep copy)
	 * @param game Game to copy
	 */
	public GameClassic(GameClassic game) {
		super(game);
	}
	
	/**
	 * Constructor
	 * @param board Game board
	 * @param list_cubes List with the cubes that are already in the board
	 * @param list_players List with the players
	 * @param currentPlayerColor First turn player color
	 */
	public GameClassic(Board board, List<Cube> list_cubes, List<Player> list_players, Color currentPlayerColor) {
		super(board, list_cubes, list_players, currentPlayerColor);
	}
	
	@Override
	public Game copyMe() {
		return new GameClassic(this);
	}
	
	@Override
	public void play() throws IllegalArgumentException {
		if (!this.pendingCubes.isEmpty()) {
			Cube c = this.pendingCubes.poll();
			
			// In case we can, we put the cube in the position and we update the board
			Cube newCube = new Cube(c.getX(), c.getY(), players.get(turnManager.getCurrentPlayerIndex()));
			this.board.addCubeInPos(newCube);
			
			this.board.update(newCube);
			
			//We check if the game finishes in this turn
			this.finished = board.isBoardFull();
			if (this.finished) {
				this.state = new GameState("p " + newCube.getX() + " " + newCube.getY(), this);
				//We add the current state to the replay
				replay.addState(new GameState("p " + newCube.getX() + " " + newCube.getY(),copyMe()));
				
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
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}		
	}
	
	@Override
	public String toString() {
		StringBuilder bf = new StringBuilder();
		bf.append("Turno: ");
		bf.append(getCurrentPlayer().toString());
		bf.append(StringUtils.LINE_SEPARATOR);
		bf.append(board.toString());
		return bf.toString();
	}
	
	@Override
	public JSONObject report() {
		JSONObject gameJSONObject = super.report();
		gameJSONObject.put("type", "GameClassic");
		return gameJSONObject;
	}
	
	@Override
	public void onTurnPlayed() {
		for(RolitObserver o : observers) {
			o.onTurnPlayed(this.state);
		}
	}

	@Override
	protected void onGameFinished() {
		for (RolitObserver o : this.observers)
			o.onGameFinished(this.players, "Players", replay, state);		
	}
	
}

