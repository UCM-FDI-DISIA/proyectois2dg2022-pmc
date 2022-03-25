package view;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JPanel;

import control.Controller;

public class BoardGUI {

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
}
