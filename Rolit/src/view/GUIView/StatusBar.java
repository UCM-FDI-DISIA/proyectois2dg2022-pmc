package view.GUIView;

import java.util.List;
import javax.swing.JLabel;
import javax.swing.JPanel;
import control.Controller;
import logic.Rival;
import replay.Replay;
import replay.GameState;

public class StatusBar extends JPanel implements RolitObserver, ReplayObserver {
	
	private static final long serialVersionUID = 1L;
	
	JLabel statusLabel;
	private String STATUS_TEXT = "Status: ";
	
	public StatusBar(Controller ctrl) {
		initGUI();
		ctrl.addObserver(this);
	}

	public StatusBar(Replay replay) {
		initGUI();
		replay.addObserver(this);
	}

	private void initGUI() {
		statusLabel = new JLabel(STATUS_TEXT);
		this.add(statusLabel);
	}
	
	@Override
	public void onRegister(GameState status) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onError(String err) {
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
	public void onGameStatusChange(GameState state) {
		changeStatus(state.getCommand());
	}
	
	@Override
	public void onReplayStatusChange(String msg) {
		changeStatus(msg);
		
	}

	@Override
	public void onReplayLeftButton() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onReplayRightButton() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTurnPlayed(GameState state) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onGameFinished(List<? extends Rival> rivals, String rival) {
		// TODO Auto-generated method stub
		
	}
	

}
