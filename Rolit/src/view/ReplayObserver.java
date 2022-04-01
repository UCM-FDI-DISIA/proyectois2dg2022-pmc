package view;

import commands.Command;
import logic.Board;
import logic.Color;
import logic.Game;

public interface ReplayObserver {
	void onReplayLeftButton();
	void onReplayRightButton();
}
