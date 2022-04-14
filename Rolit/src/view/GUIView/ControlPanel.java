package view.GUIView;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import commands.Command;
import control.Controller;
import control.SaveLoadManager;
import logic.Board;
import logic.Color;
import logic.Game;
import replay.Replay;
import replay.State;

public class ControlPanel extends JToolBar implements ActionListener, RolitObserver, ReplayObserver {

	private Game game;
	private Replay replay;
	private JFileChooser fc;
	private JButton saveFileBtn;
	private JButton replayRightBtn;
	private JButton replayLeftBtn;
	
	public ControlPanel(Game game) {
		this.game = game;
		
		//SaveFile
		saveFileBtn = new JButton();
		saveFileBtn.setActionCommand("Save");
		saveFileBtn.setIcon(new ImageIcon("resources/icons/save.png"));
		saveFileBtn.addActionListener(this);
		saveFileBtn.setMinimumSize(new Dimension(75, 20));
		this.add(saveFileBtn);
			
		fc = new JFileChooser();
	}
	
	public ControlPanel(Replay replay) {
		this.replay = replay;

		//Replay <-
		replayLeftBtn = new JButton();
		replayLeftBtn.setActionCommand("Replay left");
		replayLeftBtn.setIcon(new ImageIcon("resources/icons/replayLeft.png"));
		replayLeftBtn.addActionListener(this);
		replayLeftBtn.setMinimumSize(new Dimension(75, 20));
		this.add(replayLeftBtn);

		//Replay ->
		replayRightBtn = new JButton();
		replayRightBtn.setActionCommand("Replay right");
		replayRightBtn.setIcon(new ImageIcon("resources/icons/replayRight.png"));
		replayRightBtn.addActionListener(this);
		replayRightBtn.setMinimumSize(new Dimension(75, 20));
		this.add(replayRightBtn);

		replay.addObserver(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if (e.getActionCommand().equals("Save")) {
			int ret = fc.showSaveDialog(this);
			if (ret == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				String[] commandWords = { "s", file.getPath() };
				Command command = Command.getCommand(commandWords);
				command.execute(game);

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
	public void onCommandIntroduced(Game game, Board board, Command command) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTurnPlayed(String name, Color color) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRegister(State status) {}

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
	public void onGameFinished() {}

	@Override
	public void onGameStatusChange(State status) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFirstPlay(String name, Color color) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onReplayStatusChange(String msg) {
		// TODO Auto-generated method stub
		
	}


}
