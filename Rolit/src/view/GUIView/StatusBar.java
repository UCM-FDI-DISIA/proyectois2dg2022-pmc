package view.GUIView;

import javax.swing.JLabel;
import javax.swing.JPanel;

import commands.Command;
import commands.PlaceCubeCommand;
import control.Controller;
import logic.Board;
import logic.Color;
import logic.Game;
import logic.GameTransfer;
import replay.Replay;
import replay.State;

public class StatusBar extends JPanel implements RolitObserver, ReplayObserver {

	private GameTransfer game;
	private Replay replay;
	JLabel statusLabel;
	private String STATUS_TEXT = "Status: ";
	
	public StatusBar(GameTransfer gameTransfer) {
		this.game = gameTransfer;
		initGUI();
		gameTransfer.addObserver(this);
	}

	public StatusBar(Replay replay) {
		this.replay = replay;
		initGUI();
		replay.addObserver(this);
	}

	private void initGUI() {
		statusLabel = new JLabel(STATUS_TEXT);
		this.add(statusLabel);
	}
	
	@Override
	public void onRegister(State status) {
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
	
	private void changeStatus(String msg) {
		this.removeAll();
		statusLabel = new JLabel(STATUS_TEXT+msg);
		this.add(statusLabel);
		this.setPreferredSize(this.getPreferredSize());
		this.repaint();
		this.validate();
	}

	@Override
	public void onGameStatusChange(State status) {
		changeStatus(status);
	}
	
	@Override
	public void onReplayStatusChange(String msg) {
		changeStatus(msg);
		
	}

	@Override
	public void onFirstPlay(String name, Color color) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onReplayLeftButton() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onReplayRightButton() {
		// TODO Auto-generated method stub
		
	}

	

}
