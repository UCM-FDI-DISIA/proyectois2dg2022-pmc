package view.consoleView;

import org.json.JSONArray;

import Strategy.Strategy;
import logic.Color;

public class NewGameClassicWindow extends NewGameWindow {
	private static final String TYPE = "GameClassic";
	
	@Override
	protected boolean match(String type) {
		return TYPE.equals(type);
	}
	
	@Override
	public boolean open() {
		this.clear();
		JSONArray jPlayers = new JSONArray();	
		
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
				 if(name.endsWith(" AI")) {
						name = name.substring(0, name.length() - 3);
						System.out.println(CHOOSE_AI_DIFFICULTY_MSG);
						System.out.print(Strategy.availableStrategies());
						strat = input.nextLine();
					}
				 try {
					 // FIXME no se si al dar excepcion antes de hacer el put realmente no se hace put de nada
					 jPlayers.put(this.validatePlayer(jPlayers, name, color, strat));
					 added = true;
				 }				 
				 catch (IllegalArgumentException e) {
					System.out.println(e.getMessage());
					added = false;
				}
			}
		}
		this.json.put("players", jPlayers);
		return true;
	}

}
