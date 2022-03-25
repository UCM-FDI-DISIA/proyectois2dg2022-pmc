package view;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class CeldaGUI {

	private int x;
	private int y;
	private boolean hayMina;
	private int vecinos;
	// estado-->0 sin descubrir, estado-->1 descubierta, estado-->2 bandera
	private int estado;
	private JButton boton;
	
	/*
	 * private ImageIcon imagenMina = new ImageIcon(getClass().getResource(
			"/resources/mina.png"));
	private ImageIcon imagenBandera = new ImageIcon(getClass().getResource(
			"/resources/bandera.png"));
	private ImageIcon imagenErrorMina = new ImageIcon(getClass().getResource(
			"/resources/fallo.png"));
	 * */
	

	public CeldaGUI() {

	}

	public CeldaGUI(int y, int x, boolean hayMina) {
		this.x = x;
		this.y = y;
		this.hayMina = hayMina;
		this.estado = 0;
		this.boton = new RoundButton();

	}

	public void destaparCelda() {
		if (this.hayMina == true) {
			//this.boton.setIcon(imagenMina);
			this.boton.setBackground(Color.RED);
			return;
		} else if (this.vecinos > 0) {
			this.boton.setText(String.valueOf(vecinos));

		} else {
			this.boton.setText("");

		}
		this.boton.setBackground(Color.GRAY);
		this.boton.setBorder(null);
		this.boton.setEnabled(false);
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

	public boolean tieneMina() {
		return hayMina;
	}

	public void setMina(boolean hayMina) {
		this.hayMina = hayMina;
	}

	public int getVecinos() {
		return vecinos;
	}

	public void setVecinos(int vecinos) {
		this.vecinos = vecinos;
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
}
