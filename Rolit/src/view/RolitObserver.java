package view;

import commands.Command;
import logic.Board;
import logic.Color;
import logic.Game;

public interface RolitObserver {
	void onTurnPlayed(String name, Color color);
	void onGameFinished();
	void onCommandIntroduced(Game game, Board board, Command command);
	void onRegister(Game game, Board board, Command command);
	void onError(String err);
}
