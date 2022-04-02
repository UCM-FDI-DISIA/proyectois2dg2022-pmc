package view;

import javax.swing.JLabel;
import javax.swing.JPanel;

import commands.Command;
import commands.PlaceCubeCommand;
import control.Controller;
import logic.Board;
import logic.Color;
import logic.Game;

public class StatusBar extends JPanel implements RolitObserver {

	private Game game;
	JLabel statusLabel;
	private String STATUS_TEXT = "Status: ";
	
	public StatusBar(Game game) {
		this.game = game;
		statusLabel = new JLabel(STATUS_TEXT);
		this.add(statusLabel);
		game.addObserver(this);
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
	public void onTurnPlayed(String name, Color color) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCommandIntroduced(Game game, Board board, Command command) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onGameFinished() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChange(String msg) {
		this.removeAll();
		statusLabel = new JLabel(STATUS_TEXT+msg);
		this.add(statusLabel);
		this.setPreferredSize(this.getPreferredSize());
		this.repaint();
		this.validate();
		//statusLabel = new JLabel(STATUS_TEXT+msg);
		//this.add(statusLabel);
		//statusLabel.paintImmediately(statusLabel.getVisibleRect());
	}

	@Override
	public void onFirstPlay(String name, Color color) {
		// TODO Auto-generated method stub
		
	}

}
