package view;

import replay.Replay;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import commands.Command;
import logic.Board;
import logic.Color;
import logic.Cube;
import logic.Game;

public class CeldaGUI implements RolitObserver, ReplayObserver {

	private int x;
	private int y;
	private boolean validButton;
	private boolean filled; // Una vez se ponga un cubo no se podrá poner otro (manualmente)
	private JButton button;
	private Game game;
	private Replay replay;
	private String iconPath;
	public static final int SIDE_LENGTH = 48;
	private static final String EMPTY_ICON_PATH = "resources/icons/emptyCell.png";

	public CeldaGUI(int y, int x, boolean validButton, Game g) {
		
		
		this.x = x;
		this.y = y;
		this.validButton = validButton;
		this.filled = false;
		this.game = g;
		this.button = new JButton();
		if (validButton) {
			this.button.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {

					if (!filled) {
						String[] commandWords = { "p", Integer.toString(x), Integer.toString(y) };
						Command command = Command.getCommand(commandWords);
						command.execute(game);
					}

				}
			});
		}
		
		
		
		this.button.setEnabled(validButton);
		this.validButton = validButton;
		this.iconPath = EMPTY_ICON_PATH;
		this.button.setIcon(new ImageIcon(this.iconPath));
		this.button.setMinimumSize(new Dimension(SIDE_LENGTH, SIDE_LENGTH));
		this.button.setMaximumSize(new Dimension(SIDE_LENGTH, SIDE_LENGTH));
		this.button.setPreferredSize(new Dimension(SIDE_LENGTH, SIDE_LENGTH));
		this.button.setVisible(true);
		this.game.addObserver(this);
	}

	public CeldaGUI(int y, int x, boolean validButton, Replay replay) {
		this.x = x;
		this.y = y;
		this.replay = replay;
		this.validButton = validButton;
		this.filled = false;
		this.button = new JButton();
		this.button.setEnabled(validButton);
		this.validButton = validButton;
		this.iconPath = "resources/icons/emptyCell.png";
		this.button.setIcon(new ImageIcon(this.iconPath));
		this.button.setMinimumSize(new Dimension(SIDE_LENGTH, SIDE_LENGTH));
		this.button.setMaximumSize(new Dimension(SIDE_LENGTH, SIDE_LENGTH));
		this.button.setPreferredSize(new Dimension(SIDE_LENGTH, SIDE_LENGTH));
		this.button.setVisible(true);
		replay.addObserver(this);
	}
	
	public void resetIcon() {
		this.iconPath = EMPTY_ICON_PATH;
		this.filled = true;

		ImageIcon originalImgIcon = new ImageIcon(this.iconPath);
		Image originalImg = originalImgIcon.getImage();
		Image resizedImg = originalImg.getScaledInstance(SIDE_LENGTH, SIDE_LENGTH, java.awt.Image.SCALE_SMOOTH);
		ImageIcon resizedImgIcon = new ImageIcon(resizedImg);

		this.button.setIcon(resizedImgIcon);

		// else this.filled = false; //Por si pudiera desocuparse una casilla en una
		// replay, pero no estoy seguro de esto, porque en las replays no se debe poder
		// interactuar con el tablero

		this.button.repaint();
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public JButton getButton() {
		return button;
	}

	public void update(logic.Color newColor) {
		if (this.validButton) {
			this.iconPath = newColor.getPath();
			this.filled = true;

			ImageIcon originalImgIcon = new ImageIcon(this.iconPath);
			Image originalImg = originalImgIcon.getImage();
			Image resizedImg = originalImg.getScaledInstance(SIDE_LENGTH, SIDE_LENGTH, java.awt.Image.SCALE_SMOOTH);
			ImageIcon resizedImgIcon = new ImageIcon(resizedImg);

			this.button.setIcon(resizedImgIcon);

			// else this.filled = false; //Por si pudiera desocuparse una casilla en una
			// replay, pero no estoy seguro de esto, porque en las replays no se debe poder
			// interactuar con el tablero

			this.button.repaint();
		}
	}

	public void update() {
		Cube cube = this.game.getBoard().getCubeInPos(this.x, this.y);
		if (cube != null) {
			logic.Color newColor = cube.getColor();
			update(newColor);
		}

	}

	@Override
	public void onCommandIntroduced(Game game, Board board, Command command) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onReplayLeftButton() {
	}

	@Override
	public void onReplayRightButton() {
	}

	@Override
	public void onRegister(Game game, Board board, Command command) {

	}

	@Override
	public void onError(String err) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onGameFinished() {
	}

	@Override
	public void onTurnPlayed(String name, Color color) {
		update();
	}

	@Override
	public void onStatusChange(String msg) {
		// TODO Auto-generated method stub
		
	}

}
