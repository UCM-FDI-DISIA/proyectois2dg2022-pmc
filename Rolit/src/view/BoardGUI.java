package view;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.JPanel;

import commands.Command;
import control.SaveLoadManager;
import logic.Board;
import logic.Game;
import logic.Shape;

public class BoardGUI implements RolitObserver {

	private int nFilas;
	private int nColumnas;
	private CeldaGUI[][] celdas;
	private Game game;
	
	public BoardGUI(Game game, Shape shape) {
		this.game = game;
		
		boolean[][] shapeMatrix = SaveLoadManager.loadShape(shape);
		
		this.nFilas = shapeMatrix.length;
		this.nColumnas = shapeMatrix[0].length;
		
		this.celdas = new CeldaGUI[nFilas][nColumnas];
		
		for (int i = 0; i < nFilas; i++) {
			for (int j = 0; j < nColumnas; j++) {
				this.celdas[i][j] = new CeldaGUI(i, j, shapeMatrix[i][j], game);
			}
		}

		this.game.addObserver(this);
	}
	
	public void crearTablero(JPanel panel) {
		panel.removeAll();
		panel.setLayout(new GridLayout(nFilas, nColumnas));
		for (int i = 0; i < nFilas; i++) {
			for (int j = 0; j < nColumnas; j++) {
				panel.add(celdas[i][j].getButton());
			}
		}
		panel.revalidate();
	}

	public void update(Game game, Board board) {
		for(int i = 0; i < nFilas; i++) {
			for(int j = 0; j < nColumnas; j++) {
				celdas[i][j].update();
			}
		}
	}

	@Override
	public void onError(String err) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTurnPlayed() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCommandIntroduced(Game game, Board board, Command command) {
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
	public void onRegister(Game game, Board board, Command command) {
		update(game, board);
	}

	
}
