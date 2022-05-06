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

	// El juego funciona igual que la parte de gameClassic, el que maneja la nueva funcionalidad de los equipos es el propio player al modificar su equipo
	@Override
	public void play() throws IllegalArgumentException {
		while(!this.pendingCubes.isEmpty()) {
			Cube c = this.pendingCubes.poll();
			
			// En caso de poderse, ponemos el cubo en la posicion y actualizamos el tablero
			Cube newCube = new Cube(c.getX(), c.getY(), players.get(turnManager.getCurrentPlayerIndex()));
			this.board.addCubeInPos(newCube);
			
			this.board.update(newCube);
			// Tras actualizar las puntuaciones de cada jugador de forma correspondiente, entonces actualizamos la puntuaci�n del equipo
			for (Team team : teams)
				team.update();
			
			//Comprobamos si la partida termina con este turno
			this.finished = board.isBoardFull();
			if (this.finished) {
				onTurnPlayed();
				try {
					Thread.sleep(500); // añadimos tiempo de espera para que se coloque ultimo cubo
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				this.onGameFinished();
			}
			
			// Cambiamos el turno al siguiente jugador en la lista si la partida no ha terminado
			else {
				Cube nextCube = this.turnManager.nextTurn(this.state);//FIXME Se crea tambien en el onTurnPlayed
				if(nextCube != null) this.addCubeToQueue(nextCube);
				onTurnPlayed();
				this.executedTurn = true;
			}
			

			
			this.state = new GameState("p " + newCube.getX() + " " + newCube.getY(), this);
			//Añadimos el estado actual a replay
			replay.addState(new GameState("p " + newCube.getX() + " " + newCube.getY(),copyMe()));
			
		}
		

		
	}

	@Override
	public String toString() {
		// TODO creo que se deber�a mostrar algo de la puntuaci�n de los equipos
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
			o.onGameFinished(this.players, "Players", replay);		
	}

}
