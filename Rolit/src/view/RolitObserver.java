package view;

import commands.Command;
import logic.Board;
import logic.Game;

public interface RolitObserver {
	void onBoardCreated(Board board);
	void onGameCreated(Game game, Board board);
	void onTurnPlayed();
	void onCommandIntroduced(Game game, Board board, Command command);
	void onReplayLeftButton(Game game, Board board);
	void onReplayRightButton(Game game, Board board);
	void onRegister(Game game, Board board, Command command);
	void onError(String err);
}
