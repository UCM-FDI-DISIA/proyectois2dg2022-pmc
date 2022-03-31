package view;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import commands.Command;
import logic.Board;
import logic.Color;
import logic.Game;

public class TurnBar extends JPanel implements RolitObserver {

	private Game game;
	private JLabel msgLabel;
	private JLabel colorLabel;
	private String colorPath;
	private static final String TURN_MSG = "Turn for ";
	private static final String FINISHED_GAME_MSG = "Game has finished!";
	
	public TurnBar(Game game) {
		this.game = game;
		initGUI();
		game.addObserver(this);
	}
	
	public void initGUI() {
		msgLabel = new JLabel();
		msgLabel.setMinimumSize(new Dimension(CeldaGUI.SIDE_LENGTH, 10));
		colorLabel = new JLabel();
		colorLabel.setMinimumSize(new Dimension(CeldaGUI.SIDE_LENGTH, CeldaGUI.SIDE_LENGTH));
		this.add(msgLabel);
		this.add(colorLabel);
		this.setVisible(true);
	}
	
	@Override
	public void onTurnPlayed(String name, Color color) {	//TODO Si esto funciona dios existe
		this.msgLabel.setText(String.format("%s %s", TURN_MSG, name));
		this.colorPath = color.getPath();
		ImageIcon colorIcon = new ImageIcon(colorPath);
		Image colorImage = colorIcon.getImage();
		Image scaledColorImage = colorImage.getScaledInstance(CeldaGUI.SIDE_LENGTH, CeldaGUI.SIDE_LENGTH, Image.SCALE_SMOOTH);
		ImageIcon scaledIcon = new ImageIcon(scaledColorImage);
		this.colorLabel.setIcon(scaledIcon);
	}

	@Override
	public void onGameFinished() {
		this.msgLabel.setText(FINISHED_GAME_MSG);
	}

	@Override
	public void onCommandIntroduced(Game game, Board board, Command command) {}

	@Override
	public void onReplayLeftButton(Game game, Board board) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onReplayRightButton(Game game, Board board) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRegister(Game game, Board board, Command command) {}

	@Override
	public void onError(String err) {
		// TODO Auto-generated method stub
		
	}

}
