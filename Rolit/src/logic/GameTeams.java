package logic;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import replay.State;
import utils.StringUtils;
import view.GUIView.RolitObserver;

public class GameTeams extends Game {
	private List<Team> teams;
		
	public GameTeams(GameTeams game) {
		super(game);
		// FIXME esto puede que este mal y pero en player lo hacemos as� para las replays
		this.teams = game.teams;
	}
	
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
	public void play(int x, int y) throws IllegalArgumentException {
		// En caso de poderse, ponemos el cubo en la posicion y actualizamos el tablero
		Cube newCube = new Cube(x, y, players.get(currentPlayerIndex));
		this.board.addCubeInPos(newCube);
		
		this.board.update(newCube);
		// Tras actualizar las puntuaciones de cada jugador de forma correspondiente, entonces actualizamos la puntuaci�n del equipo
		for (Team team : teams)
			team.update();
		
		//Comprobamos si la partida termina con este turno
		this.finished = board.isBoardFull();
		if (this.finished)
			this.onGameFinished();
		
		onTurnPlayed();
		
		// Cambiamos el turno al siguiente jugador en la lista si la partida no ha terminado
		if(!this.finished)
			currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
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
		for(RolitObserver o : observers) {
			o.onTurnPlayed(new State(this.getReplayable()));
		}
	}

	@Override
	protected void onGameFinished() {
		// TODO Auto-generated method stub
		
	}

}
