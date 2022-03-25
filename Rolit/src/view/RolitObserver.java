package view;

import commands.Command;
import logic.Board;
import logic.Game;

public interface RolitObserver {
	void onGameCreated(Game game, Board board, Command command);
	void onTurnPlayed(Game game, Board board, Command command);
	void onCommandIntroduced(Game game, Board board, Command command);
	void onReplayLeftButton(Game game, Board board, Command command);
	void onReplayRightButton(Game game, Board board, Command command);
	void onRegister(Game game, Board board, Command command);
	void onError(String err);
}
