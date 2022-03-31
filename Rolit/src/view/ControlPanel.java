package view;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;

import commands.Command;
import control.Controller;
import control.SaveLoadManager;
import logic.Board;
import logic.Color;
import logic.Game;

public class ControlPanel extends JPanel implements ActionListener, RolitObserver {

	private Game game;
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
		
		//Replay <-
		replayLeftBtn = new JButton();
		replayLeftBtn.setActionCommand("Replay left");
		replayLeftBtn.setIcon(new ImageIcon("resources/icons/replayLeft.png"));
		replayLeftBtn.addActionListener(this);
		replayLeftBtn.setMinimumSize(new Dimension(75, 20));
		
		//Replay ->
		replayRightBtn = new JButton();
		replayRightBtn.setActionCommand("Replay right");
		replayRightBtn.setIcon(new ImageIcon("resources/icons/replayRight.png"));
		replayRightBtn.addActionListener(this);
		replayRightBtn.setMinimumSize(new Dimension(75, 20));
			
		fc = new JFileChooser();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if (e.getActionCommand().equals("Save")) {
			int ret = fc.showSaveDialog(this);
			if (ret == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				SaveLoadManager.saveGame(game, file.getPath());
			} else {
				//TODO Algo habrá que hacer
			}
		}
		else if(e.getActionCommand().equals("Replay left")) {
			
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
	public void onRegister(Game game, Board board, Command command) {}

	@Override
	public void onError(String err) {
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
	public void onGameFinished() {}


}
