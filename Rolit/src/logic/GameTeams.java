package logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import utils.StringUtils;

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
	public boolean play(int x, int y) {
		// Primero tenemos que comprobar que se pueda poner un cubo en la posicion
		// indicada
		if (!this.board.tryToAddCube(x, y)) {
			System.out.println("Not a valid position");
			return false;
		}
		// En caso de poderse, ponemos el cubo en la posicion y actualizamos el tablero
		Cube newCube = new Cube(x, y, players.get(currentPlayerIndex));
		this.board.addCubeInPos(newCube);
		
		this.board.update(newCube);
		// Tras actualizar las puntuaciones de cada jugador de forma correspondiente, entonces actualizamos la puntuaci�n del equipo
		for (Team team : teams)
			team.update();
		
		//Comprobamos si la partida termina con este turno
		this.finished = board.isBoardFull();
		
		// Cambiamos el turno al siguiente jugador en la lista si la partida no ha terminado
		if(!this.finished)
			currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
		return true;
	}
	
	@Override
	public String showRanking() {
		List<Team> teams = new ArrayList<Team>(this.teams);
		Collections.sort(teams);
		StringBuilder str = new StringBuilder(RANKING);
		
		str.append(StringUtils.LINE_SEPARATOR).append(MSG_REY).append(StringUtils.LINE_SEPARATOR);
		
		for (int i = 0; i < teams.size(); i++) {
			str.append(MSG_POS).append(i + 1).append(":").append(teams.get(i).toString()).append(" " + teams.get(i).getScore()).append(StringUtils.LINE_SEPARATOR);
		}
		System.out.println("");
		for (int i = 0; i < players.size(); ++i) {
			str.append(MSG_POS).append(i + 1).append(":").append(players.get(i).getName() + "from" + Team.getTeam(players.get(i)).toString() + " " + players.get(i).getScore()).append(StringUtils.LINE_SEPARATOR);
		}
		
		str.append(MSG_GOOD_LUCK).append(StringUtils.LINE_SEPARATOR);
		
		return str.toString(); 
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
	
	public static void main(String[] args) {
		Board b = new Board(Shape.SM);
		Player p1 = new Player(Color.YELLOW, "Juandi");
		Player p2 = new Player(Color.BROWN, "Leo");
		Player p3 = new Player(Color.RED, "dani");
		List<Player> lp = new ArrayList<Player>();
		List<Player> equipo1 = new ArrayList<Player>();
		List<Player> equipo2 = new ArrayList<Player>();
		equipo1.add(p1);
		equipo1.add(p2);
		equipo2.add(p3);
		Team e1 = new Team("equipo1", equipo1);
		Team e2 = new Team("equipo2", equipo2);
		lp.add(p1);
		lp.add(p2);
		List<Cube> lc = new ArrayList<Cube>();
		List<Team> lt = new ArrayList<Team>();
		lt.add(e1);
		lt.add(e2);
		Cube c = new Cube(3, 4, p2);
		Cube c2 = new Cube(3, 5, p1);
		lc.add(c);
		lc.add(c2);
		Game game = new GameTeams(b, lc, lp, Color.YELLOW, lt);
		game.play(3, 6);
		
		System.out.print(game.report());
	}
}
