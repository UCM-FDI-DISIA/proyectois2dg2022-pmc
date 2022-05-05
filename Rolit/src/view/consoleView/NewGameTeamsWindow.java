package view.consoleView;

import org.json.JSONArray;
import org.json.JSONObject;

import Strategy.Strategy;
import logic.Color;

public class NewGameTeamsWindow extends NewGameWindow {
	private static final String TYPE = "GameTeams";
	private static final int MAX_TEAMS = 2;
	private static final String CHOOSE_TEAM = String.format("Choose a team between 1-%d: ", MAX_TEAMS);
	
	@Override
	protected boolean match(String type) {
		return NewGameTeamsWindow.TYPE.equals(type);
	}
	
	@Override
	public boolean open() {
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
			 String strat = null;
			 while (!added) {
				 System.out.println(this.availableColors(jPlayers));
				 System.out.print(CHOOSE_COLOR);
				 char c = input.next().charAt(0); // HAGO QUE SEA UN STRING POR SI EL USUARIO INTRODUCE MAS DE UN CARACTER
				 input.nextLine();
				 Color color = Color.valueOfIgnoreCase(c);
				 System.out.print(CHOOSE_TEAM);
				 int selectedTeam = input.nextInt();
				 input.nextLine();
				 if(name.endsWith(" AI")) {
						name = name.substring(0, name.length() - 3);
						System.out.println(CHOOSE_AI_DIFFICULTY_MSG);
						System.out.print(Strategy.availableStrategies());
						strat = input.nextLine();
				 }
				 try {
					 // validamos los datos del jugador
					 jPlayer = this.validatePlayer(jPlayers, name, color, strat);
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
		NewGameWindow.json.put("teams", jTeams);
		NewGameWindow.json.put("players", jPlayers);
		return true;
	}
}
