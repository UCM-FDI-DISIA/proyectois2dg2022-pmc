package view;

import java.util.List;

import logic.Color;
import logic.Rival;
import replay.GameState;
import replay.Replay;

public interface RolitObserver {
	
	void onFirstPlay(GameState state);
	void onTurnPlayed(GameState state);
	// Esto es para notificar el ranking cuando acaba la partida, la lista viene ya ordenada
	void onGameFinished(List<? extends Rival> rivals, String rival, Replay replay);
	void onGameExited(Replay replay);
	// void onCommandIntroduced(Game game, Board board, Command command);
	void onRegister(GameState state);
	void onError(String err);
	// Esto sirve para pasar el nuevo estado del juego a todos los que necesiten algo cuando ha cambiado
	void onGameStatusChange(GameState state);
	
}
