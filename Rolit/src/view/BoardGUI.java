package view;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.JPanel;

import commands.Command;
import logic.Board;
import logic.Game;

public class BoardGUI implements RolitObserver {

	private int nFilas;
	private int nColumnas;
	private CeldaGUI[][] celdas;
	private Game game;
	
	public BoardGUI(int nFilas, int nColumnas, Game game) {
		this.nFilas = nFilas;
		this.nColumnas = nColumnas;
		this.game = game;

		this.celdas = new CeldaGUI[nColumnas][nFilas];
		// Crear las celdas que componen el tablero
		for (int i = 0; i < nFilas; i++)
			for (int j = 0; j < nColumnas; j++) {
				this.celdas[i][j] = new CeldaGUI(i, j, true, game);
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
				celdas[i][j].update(game, board);
			}
		}
	}

	@Override
	public void onError(String err) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onGameCreated(Game game, Board board) {
		
		this.celdas = new CeldaGUI[nFilas][nColumnas];
		
		List<List<Boolean>> shapeMatrixList = board.getShapeMatrix();
		for (int i = 0; i < nFilas; i++) {
			for (int j = 0; j < nColumnas; j++) {
				this.celdas[i][j] = new CeldaGUI(i, j, shapeMatrixList.get(i).get(j), game);
			}
		}
	}

	@Override
	public void onTurnPlayed(Game game, Board board) {
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
		// TODO Auto-generated method stub
		
	}

	
}
