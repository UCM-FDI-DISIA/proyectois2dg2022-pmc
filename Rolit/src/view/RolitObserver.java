package view;

import java.util.List;

import model.logic.Rival;
import model.replay.GameState;
import model.replay.Replay;

public interface RolitObserver {
	
	/**
	 * It notifies the observers that a turn has been executed
	 * @param state Current state of the game
	 */
	void onTurnPlayed(GameState state);
	
	/**
	 * It notifies the observers that the the game is finished
	 * @param rivals List of rivals
	 * @param rival Type of rivals: teams, players, ...
	 * @param replay Replay of the finished game
	 * @param state Current state of the game
	 */
	void onGameFinished(List<? extends Rival> rivals, String rival, Replay replay, GameState state);
	
	/**
	 * It notifies the observers that a player exit the game
	 * @param replay Replay of the finished game
	 */
	void onGameExited(Replay replay);

	/**
	 * It notifies the observers the creation of a new observer
	 * @param state Current state of the game
	 */
	void onRegister(GameState state);
	
	/**
	 * It notifies the observers that an error occurred
	 * @param err Error message
	 */
	void onError(String err);

	/**
	 * It notifies the status bar that it should show a new message
	 * @param state Current state of the game
	 */
	void onGameStatusChange(GameState state);
	
}
