package view.GUIView;

import java.util.List;
import javax.swing.JLabel;

import controller.Controller;
import model.logic.Rival;
import model.replay.GameState;
import model.replay.Replay;
import view.RolitObserver;
import view.GUIView.RolitComponents.RolitPanel;

/**
 * This class is a RolitPanel shown at the bottom of the window application
 * that shows the current status of the game or of the replay
 * @author PMC
 */
public class StatusBar extends RolitPanel implements RolitObserver, ReplayObserver {
	
	private static final long serialVersionUID = 1L;
	
	JLabel statusLabel;
	private String STATUS_TEXT = "Status: ";
	
	/**
	 * Constructor
	 * @param ctrl The controller
	 */
	public StatusBar(Controller ctrl) {
		initGUI();
		ctrl.addObserver(this);
	}

	/**
	 * Constructor
	 * @param ctrl The replay
	 */
	public StatusBar(Replay replay) {
		initGUI();
		this.changeStatus("0 / " + replay.getNumStates());
		replay.addObserver(this);
	}

	/**
	 * This method creates and shows all the components relative to this panel
	 */
	private void initGUI() {
		statusLabel = new JLabel(STATUS_TEXT);
		this.add(statusLabel);
	}
	
	/**
	 * onRegister method overridden (RolitObserver interface)
	 */
	@Override
	public void onRegister(GameState status) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * onError method overridden (RolitObserver interface)
	 */
	@Override
	public void onError(String err) {
		changeStatus(err);
		
	}
	
	/**
	 * This method changes the status bar, updating it with the message
	 * given in the parameter.
	 * @param msg New messaged to be displayed in the status bar
	 */
	private void changeStatus(String msg) {
		this.removeAll();
		statusLabel = new JLabel(STATUS_TEXT+msg);
		this.add(statusLabel);
		this.setPreferredSize(this.getPreferredSize());
		this.repaint();
		this.validate();
	}

	/**
	 * onGameStatusChange method overridden (RolitObserver interface)
	 */
	@Override
	public void onGameStatusChange(GameState state) {
		changeStatus(state.getCommand());
	}
	
	/**
	 * onReplayStatusChange method overridden (ReplayObserver interface)
	 */
	@Override
	public void onReplayStatusChange(String msg) {
		changeStatus(msg);
		
	}

	/**
	 * onReplayLeftButton method overridden (ReplayObserver interface)
	 */
	@Override
	public void onReplayLeftButton() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * onReplayRightButton method overridden (ReplayObserver interface)
	 */
	@Override
	public void onReplayRightButton() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * onTurnPlayed method overridden (RolitObserver interface)
	 */
	@Override
	public void onTurnPlayed(GameState state) {
		changeStatus("");
	}

	/**
	 * onGameFinished method overridden (RolitObserver interface)
	 */
	@Override
	public void onGameFinished(List<? extends Rival> rivals, String rival, Replay replay) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * onGameExited method overridden (RolitObserver interface)
	 */
	@Override
	public void onGameExited(Replay replay) {}
	

}
