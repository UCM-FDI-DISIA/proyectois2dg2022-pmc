package view.GUIView;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import commands.SaveCommand;
import control.Controller;
import logic.Rival;
import replay.Replay;
import replay.GameState;
import view.RolitObserver;
import view.GUIView.RolitComponents.RolitIconButton;
import view.GUIView.RolitComponents.RolitToolBar;

public class ControlPanel extends RolitToolBar implements ActionListener, RolitObserver, ReplayObserver {

	private static final long serialVersionUID = 1L;
	
	private volatile Controller ctrl;
	private Replay replay;
	private JFileChooser fc;
	private JButton saveFileBtn;
	private JButton replayRightBtn;
	private JButton replayLeftBtn;
	
	public ControlPanel(Controller ctrl) {
		this.ctrl = ctrl;
		
		//SaveFile
		saveFileBtn = new RolitIconButton(new ImageIcon("resources/icons/save.png"));
		saveFileBtn.setActionCommand("Save");
		saveFileBtn.addActionListener(this);
		this.add(saveFileBtn);
			
		fc = new JFileChooser();
	}
	
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

	@Override
	public void onRegister(GameState status) {}

	@Override
	public void onError(String err) {
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

	@Override
	public void onGameStatusChange(GameState status) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onReplayStatusChange(String msg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTurnPlayed(GameState state) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onGameFinished(List<? extends Rival> rivals, String rival, Replay replay) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFirstPlay(GameState state) {}

	@Override
	public void onGameExited(Replay replay) {}

}
