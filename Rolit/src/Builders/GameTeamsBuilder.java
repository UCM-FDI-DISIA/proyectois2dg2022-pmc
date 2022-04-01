package Builders;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import logic.Board;
import logic.Color;
import logic.Cube;
import logic.Game;
import logic.GameTeams;
import logic.Player;
import logic.Shape;
import logic.Team;

public class GameTeamsBuilder extends GameBuilder {
	public static final String TYPE = "GameTeams";
	private static final int MAX_TEAMS = 2;
	private static final String CHOOSE_TEAM = String.format("Choose a team between 1-%d: ", MAX_TEAMS);	

	GameTeamsBuilder() {
				
	}
	
	String getName(){
		return TYPE;
	}
	
	@Override
	protected boolean match(String type) {
		return TYPE.equals(type);
	}

	@Override
	protected Game GenerateGame(JSONObject o) {		
		Color turn = Color.valueOfIgnoreCase(o.getString("turn").charAt(0));
		
		List<Player> list_players = new ArrayList<Player>();
		JSONArray playersJSONArray = o.getJSONArray("players");
		for (int i = 0; i < playersJSONArray.length(); ++i) {
			JSONObject player = playersJSONArray.getJSONObject(i);
			list_players.add(new Player(Color.valueOfIgnoreCase(player.getString("color").charAt(0)), player.getString("name")));
		}			

		JSONObject boardJSONObject = o.getJSONObject("board");
		Board board = new Board(Shape.valueOfIgnoreCase(boardJSONObject.getString("shape"))); // FIXME asumo que el constructor de Board se ve	
		
		JSONArray teamsJSONArray = o.getJSONArray("teams");
		List<Team> list_teams = new ArrayList<Team>();
		for (int i = 0; i < teamsJSONArray.length(); i++) {
			JSONObject team = teamsJSONArray.getJSONObject(i);
			List<Player> list_playersTeam = new ArrayList<Player>();
			JSONArray playersTeamJSONArray = team.getJSONArray("players");
			for (int j = 0; j < playersTeamJSONArray.length(); ++j) {
				JSONObject playerTeam = playersTeamJSONArray.getJSONObject(j);
				list_playersTeam.add(new Player(Color.valueOfIgnoreCase(playerTeam.getString("color").charAt(0)), playerTeam.getString("name")));
			}
			list_teams.add(new Team(team.getString("name"), list_playersTeam));			
		}
		
		List<Cube> list_cubes = new ArrayList<Cube>();
		JSONArray cubesJSONArray = boardJSONObject.getJSONArray("cubes");
		for (int i = 0; i < cubesJSONArray.length(); ++i) {
			JSONObject cube = cubesJSONArray.getJSONObject(i);
			list_cubes.add(new Cube(cube.getJSONArray("pos").getInt(0), cube.getJSONArray("pos").getInt(1),
					Player.getPlayer(Color.valueOfIgnoreCase(cube.getString("color").charAt(0)))));
		}		
		
		return new GameTeams(board, list_cubes, list_players, turn, list_teams);
	}

	@Override
	protected void whatINeed(int nPlayers, JSONObject o) {
		JSONArray jPlayers = new JSONArray();
		JSONObject jPlayer = new JSONObject();
		JSONArray jPlayersTeam[] = new JSONArray[MAX_TEAMS];
		JSONArray jTeams = new JSONArray();
		JSONObject jTeam[] = new JSONObject[MAX_TEAMS];

		for (int i = 0; i < MAX_TEAMS; i++) {
			jTeam[i] = new JSONObject();
			jPlayersTeam[i] = new JSONArray();
			jTeam[i].put("name", String.format("Team %d", i+1));
		}			
		
		System.out.println(NAME_PLAYERS);
		System.out.println();
		
		for (int i = 0; i < nPlayers; ++i) {
			boolean added = false;
			System.out.print("Player " + (i + 1) + ": ");
			String name = input.nextLine();
			 while (!added) {
				 System.out.println(this.availableColors(jPlayers));
				 System.out.print(CHOOSE_COLOR);
				 char c = input.next().charAt(0); // HAGO QUE SEA UN STRING POR SI EL USUARIO INTRODUCE MAS DE UN CARACTER
				 input.nextLine();
				 Color color = Color.valueOfIgnoreCase(c);
				 System.out.print(CHOOSE_TEAM);
				 int selectedTeam = input.nextInt();
				 input.nextLine();
				 try {
					 // validamos los datos del jugador
					 jPlayer = this.validatePlayer(jPlayers, name, color);
					 // lo a�adimos a la lista del equipo al que pertenezca
					 jPlayersTeam[selectedTeam - 1].put(jPlayer);
					 // lo a�adimos a la lista global de players para game
					 jPlayers.put(jPlayer);
					 added = true;
				 }				 
				 catch (IllegalArgumentException e) {
					System.out.println(e.getMessage());
					added = false;
				 }
				 // FIXME posiblemente no sea la excepcion para cuando nos salimos del array
				 catch (IndexOutOfBoundsException e) {
					 System.out.println("Team index must be valid");
					 added = false;
				 }
			}
		}
		// cuando ya hemos terminado, solo tenemos que juntarlo todo
		for (int i = 0; i < MAX_TEAMS; i++) {
			jTeam[i].put("players", jPlayersTeam[i]);
			jTeams.put(jTeam[i]);
		}
		o.put("teams", jTeams);
		o.put("players", jPlayers);		
	}

	
}
