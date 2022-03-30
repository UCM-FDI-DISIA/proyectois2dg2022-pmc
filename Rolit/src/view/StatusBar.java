package view;

import javax.swing.JLabel;
import javax.swing.JPanel;

import commands.Command;
import control.Controller;
import logic.Board;
import logic.Game;

public class StatusBar extends JPanel implements RolitObserver {

	private Game game;
	
	public StatusBar(Game game) {
		this.game = game;
		JLabel statusLabel = new JLabel("Status: ");
		this.add(statusLabel);
	}

	@Override
	public void onRegister(Game game, Board board, Command command) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onError(String err) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onGameCreated(Game game, Board board) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTurnPlayed() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCommandIntroduced(Game game, Board board, Command command) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onReplayLeftButton(Game game, Board board) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onReplayRightButton(Game game, Board board) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onBoardCreated(Board board) {
		// TODO Auto-generated method stub
		
	}


}
