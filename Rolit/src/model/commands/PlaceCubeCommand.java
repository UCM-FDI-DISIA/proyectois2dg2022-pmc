package model.commands;

import model.logic.Cube;
import model.logic.Game;

/**
 * This class represents a command to place a cube in a position
 * @author PMC
 *
 */
public class PlaceCubeCommand extends Command {
	private static final String NAME = "place_cube";
	private static final String DETAILS = "[p]lace_cube <x> <y>";
	private static final String SHORTCUT = "p";
	private static final String HELP = "place a cube in position x, y";	
	private static final String COOR_IS_NUMBER_MSNG = "the coordenates must be integers";
	private int x;
	private int y;
	
	/**
	 * Constructor
	 */
	public PlaceCubeCommand() {
		super(NAME, DETAILS, SHORTCUT, HELP);
	}
	
	/**
	 * Consturctor
	 * @param x x coordinate of the cube
	 * @param y y coordinate of the cube
	 */
	public PlaceCubeCommand(int x, int y) {
		this();
		this.x = x;
		this.y = y;
	}

	@Override
	public void execute(Game game) throws Exception {
		//We simply add a cube; the game will know to add the player that is playing
		game.addCubeToQueue(new Cube(this.x, this.y, null));
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
