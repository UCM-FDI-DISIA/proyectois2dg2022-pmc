package commands;

import logic.Game;

public class PlaceCubeCommand extends Command {
	private static final String NAME = "place cube";

	private static final String DETAILS = "[p]lace cube";

	private static final String SHORTCUT = "p";

	private static final String HELP = "place a cube";
	
	private static final String COOR_IS_NUMBER_MSNG = "the coordenates must be integers";
	
	private static final String INVALID_COOR_MSG = "Las coordenadas introducidas no son válidas";

	private int x;
	private int y;
	
	public PlaceCubeCommand() {
		super(NAME, SHORTCUT, DETAILS, HELP);
	}

	@Override
	public boolean execute(Game game) {
		if(!game.play(x, y)) {
			System.out.println(INVALID_COOR_MSG);
		}
		return true;
	}
	
	@Override
	public Command parse(String[] words) {
		if(matchCommandName(words[0])) {
			if(words.length != 3)
				throw new IllegalArgumentException(String.format("[ERROR]: %s %s", INCORRECT_NUMBER_OF_ARGS_MSG, DETAILS));
			try {
				x = Integer.valueOf(words[1]);
				y = Integer.valueOf(words[2]);
			} catch (NumberFormatException nfe) {
				throw new IllegalArgumentException(COOR_IS_NUMBER_MSNG, nfe);
			}
			return this;
		}
		return null;
	}
}
