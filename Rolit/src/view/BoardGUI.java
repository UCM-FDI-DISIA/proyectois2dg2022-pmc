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
	private Controller ctrl;
	
	public BoardGUI(int nFilas, int nColumnas, Controller ctrl) {
		this.nFilas = nFilas;
		this.nColumnas = nColumnas;

		this.celdas = new CeldaGUI[nColumnas][nFilas];
		// Crear las celdas que componen el tablero
		for (int i = 0; i < nFilas; i++)
			for (int j = 0; j < nColumnas; j++) {
				this.celdas[i][j] = new CeldaGUI(i, j, true);
			}
		this.ctrl = ctrl;
	}
	
	public void crearTablero(JPanel panel) {
		panel.removeAll();
		panel.setLayout(new GridLayout(nFilas, nColumnas));
		for (int i = 0; i < nFilas; i++) {
			for (int j = 0; j < nColumnas; j++) {
				panel.add(celdas[i][j].getBoton());
			}
		}
		panel.revalidate();
	}


	@Override
	public void onError(String err) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onGameCreated(Game game, Board board, Command command) {
		
		this.celdas = new CeldaGUI[nColumnas][nFilas];
		
		List<List<Boolean>> shapeMatrixList = board.getShapeMatrix();
		// TODO Auto-generated method stub
		for (int i = 0; i < nFilas; i++)
			for (int j = 0; j < nColumnas; j++) {
				this.celdas[i][j] = new CeldaGUI(i, j, shapeMatrixList.get(i).get(j));
			}
		
	}

	@Override
	public void onTurnPlayed(Game game, Board board, Command command) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCommandIntroduced(Game game, Board board, Command command) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onReplayLeftButton(Game game, Board board, Command command) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onReplayRightButton(Game game, Board board, Command command) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRegister(Game game, Board board, Command command) {
		// TODO Auto-generated method stub
		
	}
}
