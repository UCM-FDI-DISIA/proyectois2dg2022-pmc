package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import commands.Command;
import logic.Board;
import logic.Cube;
import logic.Game;

public class CeldaGUI implements RolitObserver {

	private int x;
	private int y;
	private boolean validButton;
	private boolean filled;	//Una vez se ponga un cubo no se podrá poner otro (manualmente)
	private JButton button;
	private Game game;
	private String iconPath;

	public CeldaGUI(int y, int x, boolean validButton, Game g) {
		this.x = x;
		this.y = y;
		this.validButton = validButton;
		this.filled = false;
		this.game = g;
		this.button = new JButton();
		this.button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!filled) {
					String[] commandWords = {"p", Integer.toString(x), Integer.toString(y)};
					Command command = Command.getCommand(commandWords);
					command.execute(game);
				}
				
			}
		});
		this.button.setEnabled(validButton);
		this.iconPath = "resources/icons/emptyCell.png";
		this.button.setIcon(new ImageIcon(this.iconPath));
		this.button.setVisible(true);
		this.game.addObserver(this);
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

	public void update(Game game, Board board) {
		Cube cube = board.getCubeInPos(this.x, this.y);
		if(cube != null) {
			logic.Color newColor = cube.getColor();
			this.iconPath = newColor.getPath();
			this.button.setIcon(new ImageIcon(this.iconPath));
			this.filled = true;
			this.button.setEnabled(false);
		}
		else this.filled = false; //Por si pudiera desocuparse una casilla en una replay, pero no estoy seguro de esto, porque en las replays no se debe poder interactuar con el tablero
	}

	@Override
	public void onGameCreated(Game game, Board board) {
		update(game, board);
	}

	@Override
	public void onTurnPlayed(Game game, Board board) {
		update(game, board);
	}

	@Override
	public void onCommandIntroduced(Game game, Board board, Command command) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onReplayLeftButton(Game game, Board board) {
		update(game, board);
	}

	@Override
	public void onReplayRightButton(Game game, Board board) {
		update(game, board);
	}

	@Override
	public void onRegister(Game game, Board board, Command command) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onError(String err) {
		// TODO Auto-generated method stub
		
	}


}
