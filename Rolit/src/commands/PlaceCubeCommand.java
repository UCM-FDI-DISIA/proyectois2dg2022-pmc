package commands;

import logic.Game;

public class PlaceCubeCommand extends Command {
	private static final String NAME = "place_cube";
	private static final String DETAILS = "[p]lace_cube <x> <y>";
	private static final String SHORTCUT = "p";
	private static final String HELP = "place a cube in position x, y";	
	private static final String COOR_IS_NUMBER_MSNG = "the coordenates must be integers";	
	private static final String INVALID_COOR_MSG = "Invalid position";
	private static final String SUCCESS_MSG = "Cube placed succesfully";
	private int x;
	private int y;
	
	public PlaceCubeCommand() {
		super(NAME, DETAILS, SHORTCUT, HELP);
	}

	@Override
	public void execute(Game game) throws Exception {
		game.play(x, y);
		game.onStatusChange(String.format("%s %d % d", SHORTCUT, this.x, this.y));
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
