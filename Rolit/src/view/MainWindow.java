package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;

import commands.Command;
import control.Controller;
import logic.Board;
import logic.Game;

public class MainWindow extends JFrame implements RolitObserver, ActionListener {

	private Game game;
	private JButton createGameButton;
	private JPanel welcomePanel;
	JPanel mainPanel;
	JPanel boardPanel;
	
	public MainWindow(Game game) {
		super("Rolit");
		this.game = game;
		initGUI();
	}
	
	private void initGUI() {
		mainPanel = new JPanel(new BorderLayout());
		this.setContentPane(mainPanel);
		mainPanel.add(new ControlPanel(game), BorderLayout.PAGE_START);
		mainPanel.add(new StatusBar(game),BorderLayout.PAGE_END);
		
		welcomePanel = new JPanel(new BorderLayout());
		welcomePanel.add(new JLabel("¿Qué desea hacer?"));
		createGameButton = new JButton("Crear nueva partida");
		createGameButton.setActionCommand("crearNuevaPartida");
		createGameButton.addActionListener(this);
		welcomePanel.add(createGameButton);
		mainPanel.add(welcomePanel, BorderLayout.CENTER);
		
		boardPanel = new JPanel(new BorderLayout());
		BoardGUI tablero = new BoardGUI(8, 8, game);
		tablero.crearTablero(boardPanel);
		mainPanel.add(boardPanel, BorderLayout.CENTER);
		
		
		//boardPanel.add(new JLabel("Estadï¿½sticas: "), BorderLayout.SOUTH);
		
		this.setPreferredSize(new Dimension(500, 500));
		this.pack();
		this.setVisible(true);
	}

	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("crearNuevaPartida")) {
			CreateGameDialog dialog = new CreateGameDialog(this);
			dialog.open();
			
			mainPanel.remove(welcomePanel);
			mainPanel.add(boardPanel);
			
		}
		
	}
	@Override
	public void onGameCreated(Game game, Board board, Command command) {
		// TODO Auto-generated method stub
		
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

	@Override
	public void onError(String err) {
		// TODO Auto-generated method stub
		
	}



}
