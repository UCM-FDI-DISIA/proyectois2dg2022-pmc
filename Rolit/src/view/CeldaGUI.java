package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import commands.Command;
import control.Controller;
import logic.Board;
import logic.Cube;
import logic.Game;

public class CeldaGUI implements RolitObserver {

	private int x;
	private int y;
	private int estado;
	private boolean validButton;
	private JButton boton;
	private Game game;
	private String iconPath;
	
	public CeldaGUI() {

	}

	public CeldaGUI(int y, int x, boolean validButton, Game g) {
		this.x = x;
		this.y = y;
		this.estado = 0;
		this.validButton = validButton;
		this.game = g;
		this.boton = new JButton();
		this.boton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String[] commandWords = {"p", Integer.toString(x), Integer.toString(y)};
				Command command = Command.getCommand(commandWords);
				command.execute(game);
			}
		});
		this.iconPath = "resources/icons/emptyCell.png";
		this.boton.setIcon(new ImageIcon(this.iconPath));
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getEstado() {
		return estado;
	}

	public void setEstado(int estado) {
		this.estado = estado;
	}

	public JButton getBoton() {
		return boton;
	}

	public void setBoton(JButton boton) {
		this.boton = boton;
	}
	
	@Override
	public void onTurnPlayed(Game game, Board board, Command command) {
		Cube cube = board.getCubeInPos(this.x, this.y);
		if(cube != null) {
			logic.Color newColor = cube.getColor();
			this.iconPath = newColor.getPath();
		}
	}
}
