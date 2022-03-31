package view;

import commands.Command;
import logic.Board;
import logic.Color;
import logic.Game;

public interface RolitObserver {
	void onTurnPlayed(String name, Color color);
	void onGameFinished();
	void onCommandIntroduced(Game game, Board board, Command command);
	void onReplayLeftButton(Game game, Board board);
	void onReplayRightButton(Game game, Board board);
	void onRegister(Game game, Board board, Command command);
	void onError(String err);
}
