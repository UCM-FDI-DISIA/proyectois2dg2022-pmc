package view.consoleView;

import org.json.JSONArray;

import model.logic.Color;
import model.strategy.Strategy;

/**
 * This class displays the necessary information in order to create a new classic game, 
 * it also gathers users intentions 
 * It extends NewGameWindow
 * @author PMC
 *
 */
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
				 char c = input.next().charAt(0); //We make it a String in case the user introduces more than one character
				 input.nextLine();
				 Color color = Color.valueOfIgnoreCase(c);
				 if(name.endsWith(" AI")) {
						System.out.println(CHOOSE_AI_DIFFICULTY_MSG);
						System.out.print(Strategy.availableStrategies());
						do {
						strat = input.nextLine().toUpperCase();
						if(!validStrategy(strat)) {
							System.out.println(STRATEGY_ERROR);
						}
						} while(!validStrategy(strat));
				 }
				 try {
					 jPlayers.put(this.validatePlayer(jPlayers, name.endsWith(" AI") ? name.substring(0, name.length() - 3) : name, color, strat));
					 added = true;
				 }				 
				 catch (IllegalArgumentException e) {
					System.out.println(e.getMessage());
					added = false;
				}
			}
		}
		NewGameWindow.json.put("players", jPlayers);
		return true;
	}

}
