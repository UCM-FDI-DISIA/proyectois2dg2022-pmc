package view;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;

import commands.Command;
import control.Controller;
import logic.Board;
import logic.Game;

public class ControlPanel extends JPanel implements ActionListener, RolitObserver {

	private Game game;
	JFileChooser fc;
	
	public ControlPanel(Game game) {
		this.game = game;
		
		JButton btnOpenFile = new JButton();
		btnOpenFile.setActionCommand("abrir");
		btnOpenFile.setIcon(new ImageIcon("resources/icons/open.png"));
		btnOpenFile.addActionListener(this);
		btnOpenFile.setMinimumSize(new Dimension(75, 20));
		this.add(btnOpenFile);
		
		JButton btnSaveFile = new JButton();
		btnSaveFile.setActionCommand("guardar");
		btnSaveFile.setIcon(new ImageIcon("resources/icons/save.png"));
		btnSaveFile.addActionListener(this);
		btnSaveFile.setMinimumSize(new Dimension(75, 20));
		this.add(btnSaveFile);
		
		fc = new JFileChooser();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("abrir")) {
			int ret = fc.showOpenDialog(this);
			//seg�n salga yo de esta ventana, "ret" tomar� un valor u otro
			if (ret == JFileChooser.APPROVE_OPTION) {
				//...
			} else {
				//...
			}
		} else if (e.getActionCommand().equals("guardar")) {
			int ret = fc.showSaveDialog(this);
			if (ret == JFileChooser.APPROVE_OPTION) {
				//...
			} else {
				//...
			}
		}
		
	}


	@Override
	public void onCommandIntroduced(Game game, Board board, Command command) {
		// TODO Auto-generated method stub
		
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
