package model.strategy;

import model.logic.Color;
import model.replay.GameState;
import utils.Pair;

/**
 * This class represents de logic behind de artificial intelligences' strategies.
 * @author PMC
 * 
 */
public abstract class Strategy {

	/**
	 * Instance of SimplifiedBoard to simulate changes in the board
	 */
	protected SimplifiedBoard simplifiedBoard;
	
	/**
	 * Color of the player that owns the stategy instance
	 */
	protected Color color;
	
	/**
	 * Method used to calculate the move that the owner player will play next
	 * @param currentColor Color of the players whose turn is going to be simulated
	 * @param state Current state of the game
	 * @return coordinates where the next cube will be placed, null if the board is full
	 */
	public abstract Pair<Integer, Integer> calculateNextMove(Color currentColor, GameState state);
	
	/**
	 * Auxiliary method used to make the needed calculations for caltulateNextMove
	 * @param currentColor Color of the current player whose turn is being simulated
	 * @param depth Number of turns left to explore after any move is simulated
	 * @param alpha Alpha value for the alpha-beta prune method
	 * @param beta Beta value for the alpha-beta prune method
	 * @return The score that will be guaranteed to the player after playing the calculated move
	 */
	public abstract int simulate(Color currentColor, int depth, int alpha, int beta);
	
	/**
	 * Array of the inheriter classes
	 */
	public static Strategy[] strategies = {
		new RandomStrategy(),
		new GreedyStrategy(null),
		new MinimaxStrategy(null)
	};
	
	/**
	 * This method returns a String enumerating every Strategy and its difficulty
	 * @return String enumerating every Strategy and its difficulty
	 */
	public static String availableStrategies() {
		StringBuilder str = new StringBuilder();
		for (Strategy s : strategies) {
			str.append(String.format("%s strategy (%s difficulty)%n", s.getName(), s.getDifficulty()));
		}
		return str.toString();
	}
	
	/**
	 * Method that returns an instance of a Strategy subclass that matches the type
	 * @param color Color of the player that will own the Strategy instance
	 * @param type Type of Strategy that wants to be instanciated
	 * @return Instance of a Strategy subclass that matches the type, null if there are no matches
	 */
	public static Strategy parse(Color color, String type) {
		Strategy s = null;
		switch(type) {
		case "RANDOM":
			s = new RandomStrategy();
			break;
		case "GREEDY":
			s = new GreedyStrategy(color);
			break;
		case "MINIMAX":
			s = new MinimaxStrategy(color);
			break;
		default:
			throw new IllegalArgumentException("Not a parseable difficulty");
		}
		return s;
	}
	
	/**
	 * Getter of the strategy's name
	 * @return Strategy's name
	 */
	public abstract String getName();
	
	/**
	 * Getter of the strategy's difficulty
	 * @return Strategy's difficulty
	 */
	public abstract String getDifficulty();
	
	/**
	 * The toString() representation of the strategies is the name itself
	 */
	@Override
	public String toString() {
		return getName(); 
	}
}
