package view.GUIView;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import controller.Controller;
import model.commands.SaveCommand;
import model.logic.Rival;
import model.replay.GameState;
import model.replay.Replay;
import view.RolitObserver;
import view.GUIView.RolitComponents.RolitIconButton;
import view.GUIView.RolitComponents.RolitToolBar;

/**
 * This class is a Swing JPanel that shows, depending on the case, the button of save game and
 * the buttons that allow to go forward and backward in the replays. It is placed at the top
 * of the window of the application.
 * @author PMC
 */
public class ControlPanel extends RolitToolBar implements ActionListener, RolitObserver, ReplayObserver {

	private static final long serialVersionUID = 1L;
	
	private volatile Controller ctrl;
	private Replay replay;
	private JFileChooser fc;
	private JButton saveFileBtn;
	private JButton replayRightBtn;
	private JButton replayLeftBtn;
	
	/**
	 * Constructor given a controller (i.e. called when playing a game)
	 * @param ctrl The Controller
	 */
	public ControlPanel(Controller ctrl) {
		this.ctrl = ctrl;
		
		//SaveFile
		saveFileBtn = new RolitIconButton(new ImageIcon("resources/icons/save.png"));
		saveFileBtn.setActionCommand("Save");
		saveFileBtn.addActionListener(this);
		this.add(saveFileBtn);
			
		fc = new JFileChooser();
	}
	
	/**
	 * Constructor given a replay (i.e. called when playing a replay)
	 * @param replay The replay
	 */
	public ControlPanel(Replay replay) {
		this.replay = replay;

		//Replay <-
		replayLeftBtn = new RolitIconButton(new ImageIcon("resources/icons/replayLeft.png"), new Dimension(75, 50));
		replayLeftBtn.setActionCommand("Replay left");
		replayLeftBtn.addActionListener(this);
		this.add(replayLeftBtn);

		//Replay ->
		replayRightBtn = new RolitIconButton(new ImageIcon("resources/icons/replayRight.png"), new Dimension(75, 50));
		replayRightBtn.setActionCommand("Replay right");
		replayRightBtn.addActionListener(this);
		this.add(replayRightBtn);

		replay.addObserver(this);
	}

	/**
	 * Overridden method from ActionListener. It carries several activities depending on the button clicked
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if (e.getActionCommand().equals("Save")) {
			int ret = fc.showSaveDialog(this);
			if (ret == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				try {
					ctrl.executeCommand(new SaveCommand(file.getPath()));
				} catch (Exception e1) {
					e1.printStackTrace();
				}

			} else {
				//TODO Algo habrá que hacer
			}
		}
		else if(e.getActionCommand().equals("Replay left")) {
			replay.previousState();
		}
		else if(e.getActionCommand().equals("Replay right")) {
			replay.nextState();

		}
	}

	/**
	 * onRegister method overridden (RolitObserver interface)
	 */
	@Override
	public void onRegister(GameState status) {}

	/**
	 * onError method overridden (RolitObserver interface)
	 */
	@Override
	public void onError(String err) {
		// TODO Auto-generated method stub
		
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
	 * onGameStatusChange method overridden (RolitObserver interface)
	 */
	@Override
	public void onGameStatusChange(GameState status) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * onReplayStatusChange method overridden (ReplayObserver interface)
	 */
	@Override
	public void onReplayStatusChange(String msg) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * onTurnPlayed method overridden (RolitObserver interface)
	 */
	@Override
	public void onTurnPlayed(GameState state) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * onGameFinished method overridden (RolitObserver interface)
	 */
	@Override
	public void onGameFinished(List<? extends Rival> rivals, String rival, Replay replay) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * onGameExited method overridden (ReplayObserver interface)
	 */
	@Override
	public void onGameExited(Replay replay) {}

}
