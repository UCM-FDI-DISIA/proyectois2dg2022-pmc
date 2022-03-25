package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;

import control.Controller;

public class MainWindow extends JFrame {

	private Controller _ctrl;
	public MainWindow(Controller ctrl) {
		super("Rolit");
		_ctrl = ctrl;
		initGUI();
		// TODO Auto-generated constructor stub
	}
	
	private void initGUI() {
		JPanel mainPanel = new JPanel(new BorderLayout());
		this.setContentPane(mainPanel);
		mainPanel.add(new ControlPanel(_ctrl), BorderLayout.PAGE_START);
		mainPanel.add(new StatusBar(_ctrl),BorderLayout.PAGE_END);
		
		JPanel boardPanel = new JPanel(new BorderLayout());
		BoardGUI tablero = new BoardGUI(8, 8);
		tablero.crearTablero(boardPanel);
		mainPanel.add(boardPanel, BorderLayout.CENTER);
		
		//boardPanel.add(new JLabel("Estadísticas: "), BorderLayout.SOUTH);
		
		
		this.pack();
		this.setVisible(true);
	}

}
